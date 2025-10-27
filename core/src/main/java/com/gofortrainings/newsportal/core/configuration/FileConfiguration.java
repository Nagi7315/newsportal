package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface FileConfiguration {
	
	@AttributeDefinition(name = "File Path")
	public String csvFilePath() default "/content/dam/newsportal/Assets.csv";
	
	@AttributeDefinition(name = "Email Id")
	public String emailId();
	
}
