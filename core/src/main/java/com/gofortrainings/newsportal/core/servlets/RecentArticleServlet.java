package com.gofortrainings.newsportal.core.servlets;

import com.gofortrainings.newsportal.core.services.impl.ArticleDetailsImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/test/new-article", "/bin/newsportal/popular-article"})
/*@SlingServletResourceTypes(resourceTypes = "newsportal/popular-article",
                            extensions = {"txt", "json"},
                            selectors = {"recent", "popular"},
                            methods = {"GET"} */

public class RecentArticleServlet extends SlingAllMethodsServlet {

    @Reference
    private ArticleDetailsImpl articleDetails;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Response from Get Method");
    }

    @Override
    protected void doPost(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Response from Post Method");
    }
}
