package com.gofortrainings.newsportal.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/newsportal-rss")
public class NewsportalRssServlet extends SlingSafeMethodsServlet {

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    private static final Logger LOG = LoggerFactory.getLogger(NewsportalRssServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            ResourceResolver resourceResolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER);
            Page page = resourceResolver.adaptTo(PageManager.class).getPage("/content/newsportal/us/en");
            Iterator<Page> childPages = page.listChildren();
            Element root = document.createElement("allnews");
            document.appendChild(root);
            while (childPages.hasNext()) {
                Element news = document.createElement("news");
                root.appendChild(news);

                Page childPage = childPages.next();
                Element title = document.createElement("title");
                title.appendChild(document.createTextNode(childPage.getTitle()));
                news.appendChild(title);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(childPage.getName()));
                news.appendChild(name);

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            response.setContentType("application/xml");
            StreamResult streamResult = new StreamResult(response.getWriter());
            transformer.transform(domSource,streamResult);

        } catch (Exception e) {
            LOG.info("\n ERROR GET - {} ", e.getMessage());
        }
    }
}
