package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

@Component(service=Servlet.class)
@SlingServletPaths(value = { "/bin/newsportal/page-info" })
public class PageInfoServlet extends SlingAllMethodsServlet {
	
	// Reading all titles and paths of childpages and returning in the form of json
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
				
		ResourceResolver resolver = request.getResourceResolver();		
		PageManager pageManager = resolver.adaptTo(PageManager.class);		
		Page articlePage = pageManager.getPage("/content/newsportal/us/en/article-1");
		Iterator<Page> childPages = articlePage.listChildren();		
		JsonArrayBuilder jsonArrayObj = Json.createArrayBuilder();				
		while(childPages.hasNext()) {
			Page page = childPages.next();
		/*	if(page != null){
				String childPath = page.getPath();
				response.getWriter().write(childPath+"\n");
			} */
			JsonObjectBuilder jsonObj = Json.createObjectBuilder();	
			if(page.getTitle() != null) {
			   jsonObj.add("title", page.getTitle());
			   jsonObj.add("path", page.getPath());			
			   jsonArrayObj.add(jsonObj);			
			}
		}	
		response.getWriter().write(jsonArrayObj.build().toString());
		
	}
	// Creating page
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String pageName = request.getParameter("pageName");
		String pageTitle = request.getParameter("pageTitle");
		ResourceResolver resolver = request.getResourceResolver();		
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		try {
			pageManager.create("/content/newsportal/us/en/article-1", pageName, "/conf/newsportal/settings/wcm/templates/article-template", pageTitle);
		} catch (WCMException e) {			
			e.printStackTrace();
		}
		response.getWriter().write("Page created successfully...");
	}
	
	@Override
	protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String pageName = request.getParameter("pageName");
		ResourceResolver resolver = request.getResourceResolver();		
		PageManager pageManager = resolver.adaptTo(PageManager.class);		
		
		Page page = pageManager.getPage("/content/newsportal/us/en/"+pageName);
		if(page != null) {
		try {
			pageManager.delete(page, false);
		} catch (WCMException e) {			
			e.printStackTrace();
		}		
		response.getWriter().write("Page deleted successfully...");	
		}
	}
 
}
