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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gofortrainings.newsportal.core.services.DataManagementUtilityService;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/dataManagementUtility")
public class DataManagementUtilityServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DataManagementUtilityServlet.class);

	@Reference
	private DataManagementUtilityService dataManagementUtilityService;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String filePath = request.getParameter("filePath");
		if (action != null && action.equals("archive")) {
			String archivalPath = request.getParameter("archivalPath");
			if (archivalPath != null && filePath != null) {
				dataManagementUtilityService.downloadAssets(filePath, archivalPath);
			} else {
				response.setStatus(500);
				response.setContentType("Error: Archival path is null");
				log.info("Archival path is null");
			}
		} else {
			log.info("Action is null");
		}

		if (action != null && action.equals("unpublish")) {
			if (filePath != null) {
				dataManagementUtilityService.unpublishAssets(filePath);
			} else {
				response.setStatus(500);
				response.setContentType("Error: File path is null");
				log.info("File path is null");
			}
		} else {
			log.info("Action is null");
		}

		if (action != null && action.equals("archive")) {
			if (filePath != null) {
				dataManagementUtilityService.DeleteAssets(filePath);
			} else {
				response.setStatus(500);
				response.setContentType("Error: File path is null");
				log.info("File path is null");
			}
		} else {
			log.info("Action is null");
		}

	}

}
