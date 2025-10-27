package com.gofortrainings.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;


import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PathBasedServletTest {
	
	AemContext context = new AemContext();
	
	PathBasedServlet pathBasedServlet = new PathBasedServlet();
	
	MockSlingHttpServletRequest request;
	MockSlingHttpServletResponse response;
	
	@BeforeEach
	void setup() {		
		request = context.request();
		response = context.response();
	}

	@Test
	void doGetPathBasedServlet() throws ServletException, IOException {
		pathBasedServlet.doGet(request,response);
		
	}
	
	@Test
	void doPostPathBasedServlet() throws ServletException, IOException {
		pathBasedServlet.doPost(request,response);		
	}
	
	@Test
	void doPutPathBasedServlet() throws ServletException, IOException {
		pathBasedServlet.doPut(request,response);		
	}
	
	@Test
	void doDeletePathBasedServlet() throws ServletException, IOException {
		pathBasedServlet.doDelete(request,response);		
	}

}
