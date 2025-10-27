package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

@Component(service=Servlet.class)
@SlingServletPaths(value = { "/bin/newsportal/recent-articles" })
public class PathBasedServlet extends SlingAllMethodsServlet{
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Path based servlet - GET");
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Path based servlet - POST");
	}

	@Override
	protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Path based servlet - PUT");
	}
	
	@Override
	protected void doDelete(SlingHttpServletRequest request,SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Path based servlet - Delete");
	}
}
