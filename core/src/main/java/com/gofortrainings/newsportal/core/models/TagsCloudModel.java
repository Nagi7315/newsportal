package com.gofortrainings.newsportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Model(adaptables= {Resource.class , SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagsCloudModel {
	
	@ValueMapValue
	private String title;
	
	@ScriptVariable
	ValueMap pageProperties;
	
	@SlingObject
	ResourceResolver resolver;
	
	List<String> tagList;
	
	@PostConstruct
	public void init() {					
			String[] tags = pageProperties.get("cq:tags", String[].class);
			if(tags != null && tags.length>0) {
				tagList = new ArrayList<>();
				TagManager tagManager = resolver.adaptTo(TagManager.class);
				for(String tag : tags) {				
					Tag categoryCag = tagManager.resolve(tag);
					tagList.add(categoryCag.getTitle());
				}
			}
		}
	

	public String getTitle() {
		return title;
	}

	public List<String> getTagList() {
		return tagList;
	}
	

}
