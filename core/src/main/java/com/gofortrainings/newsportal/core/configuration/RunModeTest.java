package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Run Mode Values", description = "To get diff run mode values")
public @interface RunModeTest {

    @AttributeDefinition(name = "Run Mode Value")
    public String runModeValue();

}
