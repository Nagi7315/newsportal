package com.gofortrainings.newsportal.core.configuration;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="Contact Email", description="This email will be provided to users")
public @interface ContactUsConfig {

    @Property(label="Contact Email", description="Customers will try to contact this email!")
    String email();

    @Property(label="Country", description="Config for Country Name")
    String country();
}
