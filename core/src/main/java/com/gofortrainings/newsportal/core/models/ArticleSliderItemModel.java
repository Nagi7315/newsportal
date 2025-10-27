package com.gofortrainings.newsportal.core.models;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.api.resource.Resource;

@Model(adaptables = { Resource.class })
public class ArticleSliderItemModel {

	@ValueMapValue
	private String sliderTitle;

	@ValueMapValue
	private String sliderImg;

	@ValueMapValue
	private String pagesLinks;

	public String getSliderTitle() {
		return sliderTitle;
	}

	public String getSliderImg() {
		return sliderImg;
	}

	public String getPagesLinks() {
		return pagesLinks;
	}

}
