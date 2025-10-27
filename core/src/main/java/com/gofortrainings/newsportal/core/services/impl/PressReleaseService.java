package com.gofortrainings.newsportal.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gofortrainings.newsportal.core.services.ArticleService;

@Component(service = PressReleaseService.class)
public class PressReleaseService {
	
   private static final Logger LOG = LoggerFactory.getLogger(PressReleaseService.class);
   
   @Reference
   ArticleService articleService;
   	
	@Activate
	public void activate() {
		LOG.info(articleService.getArticles());
		LOG.info("Inside activate method");
	}
	
	@Deactivate
	public void deactivate() {
		LOG.info(articleService.getArticles());
		LOG.info("Inside deactivate method");
	}
	
	@Modified
	public void update() {
		LOG.info("Inside update method");
	}
	
}
