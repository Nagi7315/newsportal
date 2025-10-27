package com.gofortrainings.newsportal.core.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/reviews")
public class CustomerReviewServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerReviewServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try(ResourceResolver resolver = request.getResourceResolver()) {
            final String[] selectors = request.getRequestPathInfo().getSelectors();
            if (selectors.length < 2) {
                LOG.error("Selectors are not provided in the request.");
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Selectors missing\"}");
                return;
            }
            Resource parentResource = resolver.getResource("/content");
            if (parentResource != null){
                final Map<String, Object> props = new HashMap<>();
                props.put(JCR_PRIMARYTYPE, NT_UNSTRUCTURED);
                Resource reviewResource = resolver.getResource("/content/reviews");
                if (reviewResource == null){
                    resolver.create(parentResource,"reviews",props);
                    resolver.commit();
                    reviewResource = resolver.getResource("/content/reviews");
                }

                if (reviewResource != null) {
                    String productResourceName = selectors[0]+"_"+selectors[1];
                    Resource productResource = reviewResource.getChild(productResourceName);
                    if (productResource == null){
                        resolver.create(reviewResource,productResourceName,props);
                        resolver.commit();
                        productResource = reviewResource.getChild(productResourceName);
                    }

                    if (productResource != null) {
                        final String email = request.getParameter("email");
                        Resource userResource = productResource.getChild(email);
                        if(userResource == null){
                            final Map<String, Object> properties = new HashMap<>();
                            properties.put("review",request.getParameter("review"));
                            properties.put("name",request.getParameter("name"));
                            resolver.create(productResource,email,properties);
                            resolver.commit();
                            final ObjectMapper objectMapper = new ObjectMapper();
                            final String jsonResponse = objectMapper.writeValueAsString(properties);
                            response.getWriter().write(jsonResponse);
                        } else {
                            response.setStatus(SlingHttpServletResponse.SC_CONFLICT);
                            response.getWriter().write("{\"error\":\"User already exists or users resource not found\"}");
                        }
                    }
                }
            }
        }
    }
}