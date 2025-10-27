package com.gofortrainings.newsportal.core.servlets;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(AemContextExtension.class)
public class UserAuthenticationServletTest {

	AemContext context = new AemContext();

	@Mock
	private SlingHttpServletRequest request;

	@Mock
	private SlingHttpServletResponse response;

	@Mock
	private ResourceResolver resolver;

	@Mock
	private UserManager userManager;

	@Mock
	private User user;

	@InjectMocks
	private UserAuthenticationServlet servlet;

	@BeforeEach
	public void setUp(){
		MockitoAnnotations.openMocks(this);
		when(request.getResourceResolver()).thenReturn(resolver);
	}

	@Test
	void testDoGet() throws IOException, ServletException, RepositoryException {
		String userID = "test User";
		when(resolver.getUserID()).thenReturn(userID);
		when(resolver.adaptTo(UserManager.class)).thenReturn(userManager);
		when(userManager.getAuthorizable(userID)).thenReturn(user);
		servlet.doGet(request, response);
	}
}
