package com.gofortrainings.newsportal.core.servlets;

import com.day.cq.dam.api.Asset;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.gofortrainings.newsportal.core.services.FileService;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Component(service = Servlet.class, property = {"sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/marvell-dam/unpublishAssetanddelete"})
public class UnpublishAssetsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    
    @Reference
    FileService fileService;

    @Reference
    private Replicator replicator;
    
    private static final Logger log = LoggerFactory.getLogger(UnpublishAssetsServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String filePath = fileService.getCsvFilePath();
        try(ResourceResolver resolver = request.getResourceResolver()){
        Resource resource = resolver.getResource(filePath);
        Asset asset = resource.adaptTo(Asset.class);
        List<String> assetPaths = readFromCsv(asset);
        
        for (String assetPath : assetPaths) {            
            try {
                unpublishAsset(assetPath, resolver, response);               
            } catch (PersistenceException e) {
                log.error("Error unpublishing and deleting asset at path: " + assetPath, e);               
            }                     
        }     
        log.info("Successfully processed assets"); 
        response.getWriter().write("Successfully removed assets");
        }
    }
        
    private List<String> readFromCsv(Asset asset) {
        List<String> assetPaths = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(asset.getOriginal().getStream()));){
              String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String assetPath = values[0].trim().toString();
                assetPaths.add(assetPath);
            }
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
        }
        return assetPaths;
    }

    private void unpublishAsset(String assetPath, ResourceResolver resolver, SlingHttpServletResponse response)
            throws IOException {
        log.debug("Entering unpublishAsset method");
        Session session = resolver.adaptTo(Session.class);
        Resource resource = resolver.getResource(assetPath);
        if (resource != null) {
            try {
                replicator.replicate(session, ReplicationActionType.DEACTIVATE, assetPath);
                ReplicationStatus replicationStatus = resource.adaptTo(ReplicationStatus.class);
                boolean status = replicationStatus != null && replicationStatus.isDeactivated();
                if (status) {
                    log.info("Successfully unpublished asset at path: {}", assetPath);
                    response.getWriter().write("Successfully Unpublished Asset " + assetPath + "\n");
					deleteAsset(assetPath, resolver, response,resource);
                }
            } catch (ReplicationException e) {
                log.error("Error unpublishing asset at path: " + assetPath, e);
            }
        } else {
            log.warn("Asset Path is Not available: {}", assetPath);
            response.getWriter().write("Asset Path is Not available " + assetPath + "\n");
        }      
    }

    private void deleteAsset(String assetPath, ResourceResolver resolver, SlingHttpServletResponse response, Resource resource)
            throws IOException {
        log.debug("Entering deleteAsset method");
        if (resource != null) {
            try {
                resolver.delete(resource);
                resolver.commit();
                log.info("Successfully deleted asset at path: {}", assetPath);
            } catch (PersistenceException e) {
                log.error("Error deleting asset at path: " + assetPath, e);
            }
        } else {
            log.warn("Asset Path is Not available: {}", assetPath);
            response.getWriter().write("Asset Path is Not available " + assetPath + "\n");
        }
    }
}
