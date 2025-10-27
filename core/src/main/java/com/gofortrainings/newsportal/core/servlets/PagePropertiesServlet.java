package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class)
/*@SlingServletResourceTypes(
		resourceTypes = {"newsportal/components/page"},
		  extensions = {"html","txt"},
	        methods = {"GET","POST"},
	        selectors = {"sample"}
	
) */
public class PagePropertiesServlet extends SlingAllMethodsServlet {
	
	@Override
	protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response)
			throws ServletException, IOException {			
			Resource resource = request.getResource();
			ResourceResolver resolver = resource.getResourceResolver();			
			PageManager pageManager = resolver.adaptTo(PageManager.class);
			Page page = pageManager.getPage(resource.getPath());			
			ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);			
			String title = modifiableValueMap.get("jcr:title", String.class);
			response.getWriter().write(title);
	}

}
