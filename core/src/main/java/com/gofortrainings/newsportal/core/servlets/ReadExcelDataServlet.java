package com.gofortrainings.newsportal.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/newsportal/excel-data" })
public class ReadExcelDataServlet extends SlingAllMethodsServlet{
	
	private static final Logger LOG = LoggerFactory.getLogger(ReadExcelDataServlet.class);
	
	@Reference 
	ResourceResolverFactory resourceResolverFactory;
	
	@Override
	protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response)
			throws ServletException, IOException {
	/*	Object qs = request.getParameter("file");
		if (qs != null) {
			ResourceResolver resolver = request.getResourceResolver();					
			Resource resource = resolver.getResource(qs+"/jcr:content/renditions/original/jcr:content");
			Node node = resource.adaptTo(Node.class);
			if (node != null) {				
				try {
					InputStream in = node.getProperty("jcr:data").getBinary().getStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			        StringBuilder out = new StringBuilder();
			        String line;
			        while ((line = reader.readLine()) != null) {			        	
			            out.append(line);
			        }
			        reader.close();
					response.getWriter().write("File DATA ==> " + out.toString());
				} catch (Exception e) {					
					response.getWriter().write("ERROR : Not able to read, something is wrong");
					e.printStackTrace();
				}
			}else {
				response.getWriter().write("File Not Found!");
			}
		}
		else {
			response.getWriter().write("Please provide file path in page quesry string parameter e.g. ?file=/content/mysite/mypage");
		}
		response.setContentType("text/plain");
	
		*/
	}
	
	 
}
