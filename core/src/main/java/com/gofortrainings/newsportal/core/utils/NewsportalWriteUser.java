package com.gofortrainings.newsportal.core.utils;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component(service = NewsportalWriteUser.class, immediate = true)
public class NewsportalWriteUser {

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    private static final String SYSTEM_USER = "writeserviceuser";


    public ResourceResolver getResourceResolver() throws LoginException {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SYSTEM_USER);
        return resourceResolverFactory.getServiceResourceResolver(param);
    }



}
