package com.gofortrainings.newsportal.core.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.common.collect.Iterators;
import org.apache.commons.compress.utils.Lists;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsCategoryModel {

	@ValueMapValue
	private String title;

	@ValueMapValue
	@Default(values = "news-portal:categories")
	private String categoryTag;

	@SlingObject
	ResourceResolver resolver;

	Map<String, Long> childTagMap;

	@PostConstruct
	public void init() {
		childTagMap = new HashMap<>();
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		if(tagManager != null) {
			Tag tagObj = tagManager.resolve(categoryTag);
			Iterator<Tag> childTags = tagObj.listChildren();
			while (childTags.hasNext()) {
				Tag tag = childTags.next();
				childTagMap.put(tag.getTitle(), tag.getCount());
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public Map<String, Long> getChildTagMap() {
		return childTagMap;
	}

}
