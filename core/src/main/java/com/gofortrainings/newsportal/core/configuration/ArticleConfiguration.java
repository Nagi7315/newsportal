package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleConfiguration {
     
	@AttributeDefinition(name = "Article Rest API")
	public String articleRestApi() default "https://gorest.co.in/public/v2/posts";
	
	@AttributeDefinition(name = "Clone ID")
	public String cloneId() default "5184393";
	
	@AttributeDefinition(name = "Enable/Disable")
	public boolean status() default true;
}
