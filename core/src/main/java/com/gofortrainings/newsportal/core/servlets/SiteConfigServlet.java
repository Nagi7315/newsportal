package com.gofortrainings.newsportal.core.servlets;

import com.gofortrainings.newsportal.core.services.impl.SiteConfigService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Optional;

@Component(
        service = {Servlet.class},
        property = {
                "sling.servlet.paths=/bin/getsites",
                "sling.servlet.methods=GET"
        }
)
public class SiteConfigServlet extends SlingAllMethodsServlet {

    @Reference
    private SiteConfigService siteConfigService;

    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        String sitePath = request.getParameter("path");

        if (sitePath != null && !sitePath.isEmpty()) {
            // 🔹 Get specific site configuration
            Optional<SiteConfigService> siteOpt = siteConfigService.getSiteByPath(sitePath);

            if (siteOpt.isPresent()) {
                SiteConfigService site = siteOpt.get();

                JsonObject siteJson = new JsonObject();
                siteJson.addProperty("siteName", site.getSiteName());
                siteJson.addProperty("siteRootPath", site.getSiteRootPath());
                siteJson.addProperty("siteUrl", site.getSiteUrl());
                siteJson.addProperty("contactEmail", site.getContactEmail());

                response.getWriter().write(GSON.toJson(siteJson));
            } else {
                JsonObject error = new JsonObject();
                error.addProperty("error", "No site configuration found for the given path: " + sitePath);
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(GSON.toJson(error));
            }

        } else {
            // 🔹 Get all site configurations
            JsonArray jsonArray = new JsonArray();

            for (SiteConfigService site : siteConfigService.getAllSites()) {
                JsonObject siteJson = new JsonObject();
                siteJson.addProperty("siteName", site.getSiteName());
                siteJson.addProperty("siteRootPath", site.getSiteRootPath());
                siteJson.addProperty("siteUrl", site.getSiteUrl());
                siteJson.addProperty("contactEmail", site.getContactEmail());
                jsonArray.add(siteJson);
            }

            response.getWriter().write(GSON.toJson(jsonArray));
        }
    }
}
