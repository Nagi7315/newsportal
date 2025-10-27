package com.gofortrainings.newsportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListModel {

	@ValueMapValue
	private String[] articlePages;

	@SlingObject
	ResourceResolver resolver;

	List<ArticleDetailModel> articleList;

	@PostConstruct
	public void init() {
		if (articlePages != null && articlePages.length > 0) {
			articleList = new ArrayList<>();
			for (String articlePage : articlePages) {
				Resource articleResource = resolver.getResource(
						articlePage + "/jcr:content/root/container/article_grid/left-container/article_details");
				if (articleResource != null) {
					ArticleDetailModel articleDetailModel = articleResource.adaptTo(ArticleDetailModel.class);
					if (articleDetailModel != null) {
						articleList.add(articleDetailModel);
					}
				}
			}
		}
	}

	public List<ArticleDetailModel> getArticleList() {
		return articleList;
	}
}
