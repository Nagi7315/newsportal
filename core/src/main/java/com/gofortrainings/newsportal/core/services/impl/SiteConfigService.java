package com.gofortrainings.newsportal.core.services.impl;

import com.gofortrainings.newsportal.core.configuration.SiteFactoryConfig;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Component(service = SiteConfigService.class)
@Designate(ocd = SiteFactoryConfig.class, factory = true)
public class SiteConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(SiteConfigService.class);

    private String siteName;
    private String siteRootPath;
    private String siteUrl;
    private String contactEmail;

    @Activate
    @Modified
    protected void activate(SiteFactoryConfig config) {
        this.siteName = config.siteName();
        this.siteRootPath = config.siteRootPath();
        this.siteUrl = config.siteUrl();
        this.contactEmail = config.contactEmail();

        LOG.info("✅ Activated Site Config for {} at {}", siteName, siteRootPath);
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteRootPath() {
        return siteRootPath;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    @Reference(
            service = SiteConfigService.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private volatile List<SiteConfigService> siteConfigs;

    public List<SiteConfigService> getAllSites() {
        return siteConfigs;
    }

    public Optional<SiteConfigService> getSiteByPath(String path) {
        if (siteConfigs == null) return Optional.empty();

        return siteConfigs.stream()
                .filter(site -> path.startsWith(site.getSiteRootPath()))
                .findFirst();
    }
}
