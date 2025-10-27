package com.gofortrainings.newsportal.core.services.impl;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleConfiguration {

    @AttributeDefinition(name = "Article Rest API")
    public String articleRestAPI() default "https://gorest.co.in/public/v2/users/2138834/posts";

    @AttributeDefinition(name = "Enable/Disable Rest API")
    public boolean enable() default true;

}
