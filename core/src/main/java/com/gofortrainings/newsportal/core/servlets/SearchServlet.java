package com.gofortrainings.newsportal.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/search")
public class SearchServlet extends SlingSafeMethodsServlet {

//    @Reference
//    QueryBuilder queryBuilder;

//    @Override
//    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//
//        ResourceResolver resolver = request.getResourceResolver();
//        Session session = resolver.adaptTo(Session.class);
//
//        Map<String, String> predicate = new HashMap<>();
//
//        predicate.put("type","cq:Page");
//        predicate.put("path","/content/newsportal");
//
//        Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
//
//        SearchResult results = query.getResult();
//        List<Hit> hits = results.getHits();
//        JsonArrayBuilder pageJsonList = Json.createArrayBuilder();
//
//        for(Hit hit : hits){
//            try {
//                Resource resource = hit.getResource();
//                Page page = resource.adaptTo(Page.class);
//                JsonObjectBuilder pageJson = Json.createObjectBuilder();
//                pageJson.add("title",page.getTitle());
//                pageJsonList.add(pageJson);
//            } catch (RepositoryException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        response.getWriter().write(pageJsonList.build().toString());
//    }



    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String query = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/newsportal]) AND\n" +
                "\ts.[jcr:content/cq:template] = '/conf/newsportal/settings/wcm/templates/article-template'\n" +
                "\torder by s.[jcr:content/jcr:created] asc";

        ResourceResolver resolver = request.getResourceResolver();
        Iterator<Resource> results = resolver.findResources(query, Query.JCR_SQL2);
        JsonArrayBuilder pageJsonList = Json.createArrayBuilder();

        while(results.hasNext()){
           Resource resource = results.next();
            Page page = resource.adaptTo(Page.class);
            JsonObjectBuilder pageJson = Json.createObjectBuilder();
            pageJson.add("title",page.getTitle());
            pageJsonList.add(pageJson);
        }
        response.getWriter().write(pageJsonList.build().toString());
    }
}

