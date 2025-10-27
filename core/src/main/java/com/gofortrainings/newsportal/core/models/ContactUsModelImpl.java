package com.gofortrainings.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import com.gofortrainings.newsportal.core.configuration.ContactUsConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {ContactUsModel.class},
        resourceType = {ContactUsModelImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ContactUsModelImpl implements ContactUsModel {
    private static final Logger LOG = LoggerFactory.getLogger(ContactUsModelImpl.class);
    protected static final String RESOURCE_TYPE = "wknd/components/contact-us";

    private String email;

    private String country;

    @SlingObject
    ResourceResolver resourceResolver;

    @ScriptVariable
    Page currentPage;

    @PostConstruct
    public void postConstruct() {
        ContactUsConfig caConfig = getContextAwareConfig(currentPage.getPath(),resourceResolver);
        email = caConfig.email();
        country = caConfig.country();
    }

    public ContactUsConfig getContextAwareConfig(String currentPage, ResourceResolver resourceResolver) {
        String currentPath = StringUtils.isNotBlank(currentPage) ? currentPage : StringUtils.EMPTY;
        Resource contentResource = resourceResolver.getResource(currentPath);
        if (contentResource != null) {
            ConfigurationBuilder configurationBuilder = contentResource.adaptTo(ConfigurationBuilder.class);
            if (configurationBuilder != null) {
                return configurationBuilder.as(ContactUsConfig.class);
            }
        }
        return null;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getCountry() {
        return country;
    }
}
