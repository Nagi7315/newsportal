package com.gofortrainings.newsportal.core.servlets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

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

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class UploadAssetServletTest {
	
	@Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;
    
    @Mock
    private ResourceResolver resolver;
    
    @Mock
    private Resource resource;
    
    @Mock
    private AssetManager assetManager;
    
    @Mock
    private Asset asset;
    
    @Mock
    private ModifiableValueMap modifiableValueMap;
    
    @InjectMocks
    private UploadAssetServlet uploadAssetServlet;
    
    
    PrintWriter writer;
    
    @BeforeEach
    void setUp() {
    	writer = mock(PrintWriter.class);	
    }

	@Test
	void testDoget() throws IOException {	
		when(request.getParameter("fileName")).thenReturn("Example.txt");
		when(request.getResourceResolver()).thenReturn(resolver);
		when(resolver.adaptTo(AssetManager.class)).thenReturn(assetManager);	   	    
	    uploadAssetServlet.doGet(request, response);		
	}
	
	@Test
	void testSaveCustomMetadataInfo() throws IOException {
		 Resource assertResource = mock(Resource.class);
		 when(asset.adaptTo(Resource.class)).thenReturn(assertResource);
		 uploadAssetServlet.saveCustomMetadataInfo(writer, asset);
	}

}
