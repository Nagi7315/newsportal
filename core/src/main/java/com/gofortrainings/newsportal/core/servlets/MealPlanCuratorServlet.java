package com.gofortrainings.newsportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.gofortrainings.newsportal.core.services.TghClinicRestService;

@Component(service= Servlet.class)
@SlingServletPaths(value = { "/bin/rightshift/mealplan" })
public class MealPlanCuratorServlet extends SlingAllMethodsServlet{
	
	@Reference
	TghClinicRestService tghService;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.getWriter().write(tghService.getMealPlanCuratorQuestions(true));
	}

}
