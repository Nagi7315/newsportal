package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.gofortrainings.newsportal.core.services.GetListOfAssetsService;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/getListOfAsset"  })
public class GetListOfAssetServlet extends SlingAllMethodsServlet{
	
	private static final long serialVersionUID = 1L;
	
	@Reference
	GetListOfAssetsService getListOfAssetsService;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {		   
		String folderPath = request.getParameter("folderPath");
		getListOfAssetsService.getListOfAssets(folderPath,response);
	}
}
