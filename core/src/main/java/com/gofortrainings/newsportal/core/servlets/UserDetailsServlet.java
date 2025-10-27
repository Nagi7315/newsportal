package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
@Component(service=Servlet.class)
@SlingServletPaths(value = { "/bin/newsportal/user-details" })
public class UserDetailsServlet extends SlingAllMethodsServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Reading Node properties
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userId");
		ResourceResolver resolver = request.getResourceResolver();		
		Resource resource = resolver.getResource("/content/user-details/"+userId);				
	    if(resource != null) {
		   ValueMap properties = resource.getValueMap();
		   String firstName = properties.get("firstName",String.class);
		   String secondName = properties.get("secondName",String.class);
		   String email = properties.get("email",String.class);
		   response.getWriter().write("User details , First Name :"+firstName+"Second Name :"+secondName+"Email :"+email);
		}
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userId");
		ResourceResolver resolver = request.getResourceResolver();		
		Resource resource = resolver.getResource("/content/user-details/"+userId);
	    if(resource == null) {
	    	Resource parentResource = resolver.getResource("/content/user-details");
	    	Map<String,Object> properties = new HashMap<>();
	    	   String firstName = request.getParameter("firstName");
			   String secondName = request.getParameter("secondName");
			   String email = request.getParameter("email");
			   if(firstName != null) {
				   properties.put("firstName", firstName);
			   }
			   
			   if(secondName != null) {
				   properties.put("secondName", secondName);
			   }
			   
			   if(email != null) {
				   properties.put("email", email);
			   }	    	
		    resolver.create(parentResource,userId,properties) ; 			  
		    response.getWriter().write("User details added....");
		    resolver.commit();
		}
	}
	// Creating and Updating, removing the Node properties
	@Override
	protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userId");
		ResourceResolver resolver = request.getResourceResolver();		
		Resource resource = resolver.getResource("/content/user-details/"+userId);				
	    if(resource != null) {	
	  //  By using ModifiableValueMap object we can perform CRUD operations on Node properties
		ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
		   String firstName = request.getParameter("firstName");
		   String secondName = request.getParameter("secondName");
		   String email = request.getParameter("email");
		   if(firstName != null) {
			   properties.put("firstName", firstName);
		   }
		   
		   if(secondName != null) {
			   properties.put("secondName", secondName);
		   }
		   
		   if(email != null) {
			   properties.put("email", email);
		   }
		   properties.remove("email");
		   response.getWriter().write("User details updated.... ");
		   resolver.commit();		
		}
	}
	
	// Deleting Node
	@Override
	protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userId");
		ResourceResolver resolver = request.getResourceResolver();		
		Resource resource = resolver.getResource("/content/user-details/"+userId);				
	    if(resource != null) {
		  resolver.delete(resource);
		   response.getWriter().write(userId+"User is deleted.....");
		   resolver.commit();
		}
	}

}
