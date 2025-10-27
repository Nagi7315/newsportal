package com.gofortrainings.newsportal.core.configuration;

import com.gofortrainings.newsportal.core.utils.Constants;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "SitePageCreateConfiguration")
public @interface SitePageCreateConfiguration {

    @AttributeDefinition
    public String restAPI() default Constants.USERS_REST_API;

    @AttributeDefinition
    public String schedulerName() default "";

    @AttributeDefinition
    public String cronExpression() default "";

    @AttributeDefinition
    public boolean enable() default true;
}
