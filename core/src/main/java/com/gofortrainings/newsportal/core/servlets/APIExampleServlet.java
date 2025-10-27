package com.gofortrainings.newsportal.core.servlets;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.gofortrainings.newsportal.core.utils.NewsportalWriteUser;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class, immediate = true)
@SlingServletPaths(value = { "/bin/newsportal/api-example"  })
public class APIExampleServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIExampleServlet.class);

    @Reference
    NewsportalWriteUser newsportalWriteUser;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws ServletException, IOException {

        // /content/country     ->  node & property

        ResourceResolver requestResolver = request.getResourceResolver();
        Resource countryRes = requestResolver.getResource("/content/country/India");
        ModifiableValueMap countryValueMap = countryRes.adaptTo(ModifiableValueMap.class);

        countryValueMap.put("capital", "Delhi");
    //    requestResolver.create(countryRes, "India", params);
        requestResolver.commit();

        try {
           ResourceResolver subServiceResolver = newsportalWriteUser.getResourceResolver();

            PageManager pageManager = subServiceResolver.adaptTo(PageManager.class);

            String pageName = request.getParameter("pageName");
            String title = request.getParameter("title");
            String patentPath = request.getParameter("patentPath");

            try {
                pageManager.create(patentPath, pageName,
                        "/conf/newsportal/settings/wcm/templates/article-template", title);

            } catch (WCMException e) {
                throw new RuntimeException(e);
            }

        } catch (LoginException e) {
            throw new RuntimeException(e);
        }


    }
}
