package com.gofortrainings.newsportal.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.gofortrainings.newsportal.core.models.CountryInfo;
import com.gofortrainings.newsportal.core.services.CountryDetailsService;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.NewsportalServiceUtils;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/countryDataServlet")
public class CountryDataServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(CountryDataServlet.class);

    @Reference
    CountryDetailsService countryDetailsService;

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    @Reference
    NewsportalServiceUtils newsportalServiceUtils;


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            String countryData = countryDetailsService.fetchCountryDetails();
            List<CountryInfo> countryDataList = parseCountryData(countryData);
            DataSource dataSource = getDataSource(countryDataList, resolver);
            request.setAttribute(DataSource.class.getName(), dataSource);
        }catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static DataSource getDataSource(List<CountryInfo> countryDataList, ResourceResolver resolver) {
        List<Resource> resourceList = new ArrayList<>();
        ValueMap valueMap;
        for (CountryInfo countryInfo : countryDataList) {
            valueMap = new ValueMapDecorator(new HashMap<>());
            valueMap.put("value", countryInfo.getCountryCode());
            valueMap.put("text", countryInfo.getCountryName());
            Resource optionResource = new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured",valueMap);
            resourceList.add(optionResource);
        }

        return new SimpleDataSource(resourceList.iterator());
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String countryData = countryDetailsService.fetchCountryDetails();
        List<CountryInfo> countryDataList = parseCountryData(countryData);
        for (CountryInfo countryInfo : countryDataList) {
            createCountryResource(countryInfo);
        }
    }

    private List<CountryInfo> parseCountryData(String countryData) {
        List<CountryInfo> countryDataList = new ArrayList<>();
        JsonObject jsonObjData = newsportalServiceUtils.parseJsonFromString(countryData);
        JsonObject data = jsonObjData.getAsJsonObject("data");
        Set<Map.Entry<String, JsonElement>> jsonResult = data.entrySet();
        for (Map.Entry<String, JsonElement> jsonObjList : jsonResult) {
            String countryCode = jsonObjList.getKey();
            JsonObject jsonObject = jsonObjList.getValue().getAsJsonObject();
            countryDataList.add(new CountryInfo(countryCode, jsonObject.get("country").getAsString(), jsonObject.get("region").getAsString()));
        }
        return countryDataList;
    }

    private void createCountryResource(CountryInfo countryInfo) {
        Map<String, Object> props = new HashMap<>();
        try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            Resource etcResource = resolver.getResource("/etc");
            Resource parentResource;
            if (etcResource != null) {
                parentResource = resolver.getResource("/etc/countries");
                if (parentResource == null) {
                    try {
                        props.put(JCR_PRIMARYTYPE, NT_UNSTRUCTURED);
                        resolver.create(etcResource, "countries", props);
                        resolver.commit();
                    } catch (PersistenceException e) {
                        log.error("Error while creating the countries Resource");
                    }
                }
                parentResource = resolver.getResource("/etc/countries");
                if (parentResource != null) {
                    Resource countryResource = resolver.getResource("/etc/countries/" + countryInfo.getCountryCode());
                    if (countryResource == null) {
                        try {
                            props.put("country", countryInfo.getCountryName());
                            props.put("region", countryInfo.getRegion());
                            resolver.create(parentResource, countryInfo.getCountryCode(), props);
                            resolver.commit();
                        } catch (PersistenceException e) {
                            log.error("Error while creating the countryResource");
                        }
                    }
                }
            }
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } 
    }
}
