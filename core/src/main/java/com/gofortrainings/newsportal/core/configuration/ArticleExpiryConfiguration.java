package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Article Expiry config")
public @interface ArticleExpiryConfiguration {
	
	@AttributeDefinition(name = "Enable/Disable Scheduler")
	public boolean enable()  default true;
	
	@AttributeDefinition(name = "Scheduler Name")
	public String schedulerName()  default "article-expiry";
	
	@AttributeDefinition(name = "Scheduler Cron Expression")
	public String schedulerExpression()  default "*/5 * * ? * *";
	
}
