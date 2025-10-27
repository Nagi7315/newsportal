package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = {"newsportal/components/page"},
        extensions = {"json", "txt"},
        methods = {"GET", "POST"},
        selectors = {"featured"}
)
public class ResourceBasedServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page page = pageManager.getPage("/content/newsportal/us/en");
        PrintWriter pw = response.getWriter();
        if (page != null) {
            Iterator<Page> pageList = page.listChildren();
            while (pageList.hasNext()) {
                Page childPage = pageList.next();
                pw.println("Page title : " + childPage.getTitle());
            }
        }

    }
}
