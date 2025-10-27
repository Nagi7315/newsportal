package com.gofortrainings.newsportal.core.servlets;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/tagsresult")
public class TagSearchServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String path = request.getParameter("contentTreePath");
        String tagStr = request.getParameter("tagId");

        ResourceResolver resolver = request.getResourceResolver();
        //TagManager tagManager = resolver.adaptTo(TagManager.class);
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page rootPage = pageManager.getPage(path);
        boolean status = checkTags(rootPage, tagStr);
        response.getWriter().write("Response : " + status);
    }

    public boolean checkTags(Page page, String tagStr) {
        boolean status = false;
        Iterator<Page> childPages = page.listChildren();
        while (childPages.hasNext()) {
            Page childPage = childPages.next();
            Tag[] tags = childPage.getTags();
            for (Tag tag : tags) {
                if (tag.getTagID().equals(tagStr)) {
                    status = true;
                    break;
                }
            }
            if (childPage.listChildren().hasNext()) {
                checkTags(childPage, tagStr);
            }
        }
        return status;
    }
}
