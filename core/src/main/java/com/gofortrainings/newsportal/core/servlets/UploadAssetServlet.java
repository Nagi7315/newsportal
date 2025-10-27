package com.gofortrainings.newsportal.core.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/upload/asset" })
public class UploadAssetServlet extends SlingAllMethodsServlet {
	
	private static final long serialVersionUID = 1L;
		  
	@Override
	  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
	    response.setHeader("Content-Type", "text/html");
	    try {
	      String fileName = request.getParameter("fileName");
	      ResourceResolver r = request.getResourceResolver();	      	      	      
	      String[] split = fileName.split("\\.");
	      String mimeType = "";
	      AssetManager assetMgr = r.adaptTo(AssetManager.class);
	      if(split.length>=1) {
	    	 mimeType = split[1];
			}
	      InputStream is = null;
	      File fi = new File("/Users/j nagireddy/Downloads/" + fileName);	     
	      is = new FileInputStream(fi);
	      String newFile = "/content/dam/" + fileName;	      
	      Asset asset = assetMgr.createAsset(newFile, is, mimeType, true);
	      r.commit();
	      response.getWriter().write("<p>File uploaded</p>");
	    }
	    catch (IOException e) {
            
	    }
	  }

	  void saveCustomMetadataInfo(PrintWriter pw, final Asset asset) {
	    Resource assetResource = asset.adaptTo(Resource.class);
	    String customPropName = "prop1";
	    String customPropValue = "prop1Val";

	    if (assetResource != null) {
	      assetResource = assetResource.getChild(JcrConstants.JCR_CONTENT + "/metadata");
	    }

	    if (assetResource != null) {
	    	ModifiableValueMap assetProperties = assetResource.adaptTo(ModifiableValueMap.class);

	      if (assetProperties != null) {
	        assetProperties.put(customPropName, customPropValue);
	        pw.write("<p>Metadta is set</p>");
	      }
	    }
	  }

}
