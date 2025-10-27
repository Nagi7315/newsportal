package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Site Configuration - Factory Config",
        description = "Factory configuration for different AEM sites (Newsportal, ITC-RightShift, etc.)"
)
public @interface SiteFactoryConfig {

    @AttributeDefinition(
            name = "Site Name",
            description = "The readable name of the site (e.g. News Portal, ITC RightShift)"
    )
    String siteName();

    @AttributeDefinition(
            name = "Site Root Path",
            description = "Root content path of the site (e.g. /content/newsportal)"
    )
    String siteRootPath();

    @AttributeDefinition(
            name = "Site URL",
            description = "Public URL of the site (e.g. https://www.newsportal.com)"
    )
    String siteUrl();

    @AttributeDefinition(
            name = "Author Contact Email",
            description = "Email ID for site-specific queries"
    )
    String contactEmail() default "";
}

