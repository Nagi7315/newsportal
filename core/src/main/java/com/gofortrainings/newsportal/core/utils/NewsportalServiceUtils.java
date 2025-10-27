package com.gofortrainings.newsportal.core.utils;

import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.jsoup.Jsoup;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Iterator;
import java.util.Objects;

@Component(service = NewsportalServiceUtils.class)
public class NewsportalServiceUtils {

    public JsonObject parseJsonFromString(String data){
        JsonObject jsonObjData = new JsonObject();
        if(data != null){
            Gson gson = new Gson();
            jsonObjData = gson.fromJson(data, JsonObject.class);
        }
        return jsonObjData;
    }

    public String json2String(JsonObject jsonObj){
        String data;
        if(jsonObj != null){
            data = jsonObj.toString();
        } else {
            data = StringUtils.EMPTY;
        }
        return data;
    }

    public Resource getComponentResource(Resource resource, String resourceType){
        if(resource != null && resource.isResourceType(resourceType)){
            return resource;
        } if (resource != null && resource.hasChildren()) {
            Iterator<Resource> resourceList = resource.listChildren();
            while (resourceList.hasNext()) {
                Resource childResource = resourceList.next();
                Resource componentResource = getComponentResource(childResource,resourceType);
                if(componentResource !=null && componentResource.isResourceType(resourceType)){
                    return componentResource;
                }
            }
        }
        return null;
    }

    public String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public String getCountryCode(final Page currentPage) {
        Page languagePage = getLanguagePage(currentPage);
        final String country = "us";
        if (Objects.nonNull(languagePage) && Objects.nonNull(languagePage.getParent())
                && Objects.nonNull(languagePage.getParent().getName())) {
            return languagePage.getParent().getName();
        }
        return country;
    }

    public Page getLanguagePage(final Page currentPage) {
        if (Objects.nonNull(currentPage)) {
            return currentPage.getAbsoluteParent(3);
        }
        return null;
    }

    public String getLanguageCode(final Page currentPage) {
        if (Objects.nonNull(currentPage)) {
            return currentPage.getLanguage(Boolean.FALSE).getLanguage();
        }
        return StringUtils.EMPTY;
    }
}
