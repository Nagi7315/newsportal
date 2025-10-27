package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface PracticeConfiguration {

    @AttributeDefinition(name = "Rest API")
    public String restApi() default "xxxxxxxxx";

    @AttributeDefinition(name = "API Key")
    public String apiKey();

}
