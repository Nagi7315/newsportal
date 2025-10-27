package com.gofortrainings.newsportal.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ResourceResolverUtil.class)
public class ResourceResolverUtil {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public ResourceResolver getResolver(String subservice) throws LoginException {
        ResourceResolver resolver = null;
        if (StringUtils.isNotBlank(subservice)){
            final Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, subservice);
            resolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
        }

        return resolver;
    }
}
 