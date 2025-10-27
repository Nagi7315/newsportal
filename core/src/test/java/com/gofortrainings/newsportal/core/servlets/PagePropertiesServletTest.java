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
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PagePropertiesServletTest {
	
    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;
    
    @Mock
    private ResourceResolver resolver;

    @Mock
    private Resource resource;
    
    @Mock
    private PageManager pageManager;

    @Mock
    private Page page; 
    
    @Mock
    private ModifiableValueMap modifiableValueMap;

    @InjectMocks
    private PagePropertiesServlet pagePropertiesServlet;

    @BeforeEach
    void setUp() {
    	when(request.getResource()).thenReturn(resource);
		when(resource.getResourceResolver()).thenReturn(resolver);
    }

	@Test
	void doGetTest() throws ServletException, IOException {	
		when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		when(pageManager.getPage(resource.getPath())).thenReturn(page);
		when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(modifiableValueMap);
		PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
		pagePropertiesServlet.doGet(request, response);
		
	}

}
