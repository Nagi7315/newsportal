package com.gofortrainings.newsportal.core.models;

import com.gofortrainings.newsportal.core.utils.NewsportalServiceUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleDetailModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleDetailModel.class);

	@ValueMapValue
	private String articleTitle;

	@ValueMapValue
	private String articleDesc;

	@ValueMapValue
	private String articleImg;

	@OSGiService
	NewsportalServiceUtils newsportalServiceUtils;

	@ValueMapValue
	private String description;

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getArticleDesc() {
		return articleDesc;
	}

	public String getArticleImg() {
		return articleImg;
	}

	public String getDescription() {
		this.description = newsportalServiceUtils.html2text(description);
		return description;
	}
}
