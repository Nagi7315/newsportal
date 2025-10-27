package com.gofortrainings.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class CreateUserDetailServletTest {
	
	AemContext context = new AemContext();
	
	@Mock
	private SlingHttpServletRequest request;

	@Mock
	private SlingHttpServletResponse response;

	@Mock
	private ResourceResolver resolver;

	@Mock
	private Resource resource;

	@Mock
	private ModifiableValueMap modifiableValueMap;

	@InjectMocks
	private CreateUserDetailServlet createUserDetailServlet = new CreateUserDetailServlet();
	
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
	//	MockitoAnnotations.initMocks(this);  Deprecated
		when(request.getResourceResolver()).thenReturn(resolver);		
	}
	
	@Test
	void doPostTest() throws ServletException, IOException {
		when(request.getParameter("email")).thenReturn("jvn@gamil.com");
		when(resolver.getResource("/content/user-details/jvn@gamil.com")).thenReturn(null);
		Resource parentResource = mock(Resource.class);
	    when(resolver.getResource("/content/user-details")).thenReturn(parentResource);
		when(request.getParameter("firstName")).thenReturn("jagiri");
		when(request.getParameter("lastName")).thenReturn("veera");
		when(request.getParameter("age")).thenReturn("25");
		when(request.getParameter("address")).thenReturn("Hyd");
		PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
		createUserDetailServlet.doPost(request, response);
	}

}
