package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.servlets.annotations.SlingServletPaths;


@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/util/node/children" })
public class GetChildNodeServlet extends SlingSafeMethodsServlet{
	
	private static final long serialVersionUID = 2598426539166789516L;
	
	private static final Logger log = LoggerFactory.getLogger(GetChildNodeServlet.class);

	List<Node> childrenList = null;

	@Override
	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp)
			throws ServerException, IOException {
		try {

			childrenList = new ArrayList<Node>();
			 resp.setHeader("Content-Type", "text/html");
			PrintWriter pw = resp.getWriter();
					Node node = req.getResourceResolver().getResource("/content/newsportal/us/en/article-1" + "/jcr:content")
					.adaptTo(Node.class);

			collectChildList(node);			
			Iterator<Node> it = childrenList.iterator();
			while (it.hasNext()) {				
				pw.write(it.next().getPath()+"<br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resp.getWriter().close();
		}
	}
	
	private void collectChildList(Node node) {
		try {
			childrenList.add(node);
			if (node.hasNodes()) {
				NodeIterator ni = node.getNodes();
				while (ni.hasNext()) {					
					collectChildList(ni.nextNode());					
				}
			}
		} catch (RepositoryException e) {
			log.info("In Catch block");
			e.printStackTrace();
		}
	}


}
