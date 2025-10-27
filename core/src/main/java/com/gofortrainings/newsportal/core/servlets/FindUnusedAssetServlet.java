package com.gofortrainings.newsportal.core.servlets;

import com.day.cq.replication.ReplicationStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Component(service = {Servlet.class})
//@SlingServletPaths("/bin/unusedAssets")
@SlingServletResourceTypes(
        resourceTypes = { "newsportal/components/unused-asset-finder"
})
@ServiceDescription("Unused Assets CSV Report")
public class FindUnusedAssetServlet extends SlingAllMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(FindUnusedAssetServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String contentPath = request.getParameter("contentPath");
        String assetFolderPath = request.getParameter("assetFolderPath");

        if (!StringUtils.isBlank(contentPath) && !StringUtils.isBlank(assetFolderPath)) {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"unused-assets.csv\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                StringBuilder csvBuilder = new StringBuilder();

                // Add CSV headers
                csvBuilder.append("Sl.No,Asset Path\r\n");

                try (ResourceResolver resolver = request.getResourceResolver()) {
                    String query = "SELECT * FROM [dam:Asset] AS s WHERE ISDESCENDANTNODE([" + assetFolderPath + "])";
                    Iterator<Resource> assetList = resolver.findResources(query, Query.JCR_SQL2);

                    int count = 1;
                    while (assetList.hasNext()) {
                        Resource assetResource = assetList.next();
                        String assetPath = assetResource.getPath();

                        ReplicationStatus replicationStatus = assetResource.adaptTo(ReplicationStatus.class);
                        if (replicationStatus != null && !replicationStatus.isDelivered()) {
                            String pageQuery = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([" + contentPath + "]) and CONTAINS(s.*, '" + assetPath + "')";
                            Iterator<Resource> pageList = resolver.findResources(pageQuery, Query.JCR_SQL2);

                            if (!pageList.hasNext()) {
                                // Append asset details to CSV
                                csvBuilder.append(count).append(",").append(assetPath).append("\r\n");
                                count++;
                            }
                        }
                    }
                }

                // Write CSV data
                byte[] csvBytes = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(csvBytes);
                outputStream.flush();
            }
        } else {
            log.error("Missing required parameters");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required parameters");
        }
    }
}