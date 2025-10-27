package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/newsportal/user" })
public class CreateUserDetailServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String emailParam = request.getParameter("email");
		ResourceResolver resolver = request.getResourceResolver();
		Resource resource = resolver.getResource("/content/user-details/" + emailParam);
		if (resource == null) {
			Resource parentResource = resolver.getResource("/content/user-details");
			Map<String, Object> properties = new HashMap<>();
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String age = request.getParameter("age");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			if (firstName != null) {
				properties.put("firstName", firstName);
			}

			if (lastName != null) {
				properties.put("lastName", lastName);
			}

			if (age != null) {
				properties.put("age", age);
			}
			if (address != null) {
				properties.put("address", address);
			}

			if (email != null) {
				properties.put("email", email);
			}
			resolver.create(parentResource, emailParam, properties);
			response.getWriter().write("User details added....");
			resolver.commit();
		}
	}

}
