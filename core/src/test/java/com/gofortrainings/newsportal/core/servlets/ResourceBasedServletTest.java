package com.gofortrainings.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ResourceBasedServletTest {
	
	ResourceBasedServlet resourceBasedServlet = new ResourceBasedServlet();

	  @Test
	  void doGet(AemContext context) throws ServletException, IOException {        
	        MockSlingHttpServletRequest request = context.request();
	        MockSlingHttpServletResponse response = context.response();
	        resourceBasedServlet.doGet(request, response);

	    }

}
