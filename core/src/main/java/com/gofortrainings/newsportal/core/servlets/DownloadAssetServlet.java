package com.gofortrainings.newsportal.core.servlets;

import  com.day.cq.dam.api.Rendition;
import com.day.cq.dam.api.Asset;
import org.apache.commons.io.FileUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Component(service = Servlet.class, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/marvell-dam/downloadasset"
})
public class DownloadAssetServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(DownloadAssetServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String folderPath = "/content/dam/newsportal/images";       
        ResourceResolver resolver = request.getResourceResolver();
        Resource folderResource = resolver.getResource(folderPath);

        try {
            if (folderResource != null) {
                Iterator<Resource> childResources = folderResource.listChildren();
                while (childResources.hasNext()) {
                    Resource assetResource = childResources.next();
                    if (isAsset(assetResource, folderPath)) {
                        downloadAsset(assetResource,folderPath);
                    }
                }
                response.getWriter().write("Successfully downloaded assets in the folder");
            } else {
                response.getWriter().write("Folder not found at path: " + folderPath);
            }
        } catch (Exception e) {
            log.error("Error: ", e);
            response.getWriter().write("Error: " + e.getMessage());
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    private boolean isAsset(Resource resource, String folderPath) {
        if (resource.isResourceType("dam:Asset") && resource.getPath().startsWith(folderPath)) {
            return true;
        }
        return false;
    }
    private void downloadAsset(Resource assetResource,  String folderPath) throws IOException {
        Asset asset = assetResource.adaptTo(Asset.class);        
        if (asset != null) {
            Rendition original = asset.getOriginal();            
            if (original != null) {
                InputStream inputStream = original.adaptTo(InputStream.class);
                if (inputStream != null) {
                    String downloadDir = "C:" + File.separator + "Users" + File.separator + "j nagireddy" + File.separator + "OneDrive" + File.separator + "DamAsset";
                    String fileName = assetResource.getName();
                    File targetFile = new File(downloadDir, fileName);
                    FileUtils.copyInputStreamToFile(inputStream, targetFile);
                    log.debug("Downloaded asset: " + targetFile.getAbsolutePath());
                } else {
                    log.warn("InputStream is null for asset: {}", assetResource.getPath());
                }
            } else {
                log.warn("No valid rendition found for asset: {}", assetResource.getPath());
            }
        } else {
            log.warn("Skipping non-asset resource: {}", assetResource.getPath());
        }
    }
}
