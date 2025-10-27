package com.gofortrainings.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import static org.mockito.Mockito.*;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class UserDetailsServletTest {

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
	private UserDetailsServlet userDetailsServlet;

	@BeforeEach
	void setUp() throws IOException {
		when(request.getResourceResolver()).thenReturn(resolver);
		when(request.getParameter("userId")).thenReturn("123");
		when(resolver.getResource("/content/user-details/123")).thenReturn(resource);
		PrintWriter writer = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(writer);

	}

	@Test
	void testDoGet() throws IOException, ServletException {
		ValueMap properties = mock(ValueMap.class);
		when(resource.getValueMap()).thenReturn(properties);
		userDetailsServlet.doGet(request, response);
	}

	@Test
	void testDoPost() throws IOException, ServletException {
		when(resolver.getResource("/content/user-details/123")).thenReturn(null);
		Resource parentResource = mock(Resource.class);
		when(resolver.getResource("/content/user-details")).thenReturn(parentResource);
		userDetailsServlet.doPost(request, response);
	}

	@Test
	void testDoPut() throws IOException, ServletException {
		when(request.getParameter("firstName")).thenReturn("John");
		when(request.getParameter("secondName")).thenReturn("Doe");
		when(request.getParameter("email")).thenReturn("john.doe@example.com");
		when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(modifiableValueMap);
		PrintWriter writer = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(writer);
		userDetailsServlet.doPut(request, response);
	}

	@Test
	void testDoDelete() throws IOException, ServletException {
		userDetailsServlet.doDelete(request, response);
	}
}