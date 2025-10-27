package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/newsportal/search-page"  })
public class ArticlePageServlet extends SlingAllMethodsServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {


		// This query will fetch the five pages based on below template path and sling:resourceType in desending order
		String query = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/newsportal])\r\n" + 
				"             AND (s.[jcr:content/cq:template] ='/conf/newsportal/settings/wcm/templates/article-template'\r\n" + 
				"             OR s.[jcr:content/sling:resourceType] ='newsportal/components/page')\r\n" + 
				"             order by s.[jcr:content/jcr:created] desc option(limit 5)";
		ResourceResolver resolver = request.getResourceResolver();		
		Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);
		JsonArrayBuilder jsonArrayObj = Json.createArrayBuilder();
		while(result.hasNext()) {
			Resource resource = result.next();
			Page page = resource.adaptTo(Page.class);
			JsonObjectBuilder jsonObj= Json.createObjectBuilder();
			jsonObj.add("title", page.getTitle());
			jsonObj.add("path", page.getPath());
			jsonArrayObj.add(jsonObj);			
		}
		response.getWriter().write(jsonArrayObj.build().toString());
		
	}

}
