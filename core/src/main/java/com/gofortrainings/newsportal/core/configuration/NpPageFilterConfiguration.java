package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface NpPageFilterConfiguration {

    @AttributeDefinition(name ="Page Property")
    public String pageProperty() default "cq:tags";
}
