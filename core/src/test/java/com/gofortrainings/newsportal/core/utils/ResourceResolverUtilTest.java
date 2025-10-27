package com.gofortrainings.newsportal.core.utils;

import static org.mockito.Mockito.*;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ResourceResolverUtilTest {

    @Mock
    private ResourceResolverFactory resourceResolverFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @InjectMocks
    private ResourceResolverUtil resourceResolverUtil;

    private String subservice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subservice = Constants.VEERA_SERVICE_USER;
    }

    @Test
    void testNewResolver() throws LoginException {
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
        resourceResolverUtil.getResolver(subservice);
        verify(resourceResolverFactory).getServiceResourceResolver(anyMap());
    }

    @Test
    void testNewResolverWithLoginException() throws LoginException {
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    }
}
