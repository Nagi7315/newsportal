package com.gofortrainings.newsportal.core.models;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CSVReaderModel {

	private static final Logger logger = LoggerFactory.getLogger(CSVReaderModel.class);

	@ValueMapValue
	private String csvPath;

	@SlingObject
	ResourceResolver resolver;

	public String getCsvPath() {
		return csvPath;
	}

	public String parentPath = "/content/newsportal/us/en";
	public String templatePath = "/conf/newsportal/settings/wcm/templates/article-template";

	@PostConstruct
	public void init() {

		logger.info("Inside init method");
		try {
			JsonArray array = JsonParser.parseString(csvPath).getAsJsonArray();
			Session session = resolver.adaptTo(Session.class);

			for (int i = 0; i < array.size(); i++) {
				JsonObject jsonObj = JsonParser.parseString(array.get(i).toString()).getAsJsonObject();

				String originalName = jsonObj.get("Name").toString();
				String pageTitle = originalName.substring(1, originalName.length() - 1);
				String pageName = pageTitle.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
				String description = jsonObj.get("description").toString();

				PageManager pageManager = resolver.adaptTo(PageManager.class);

				Page page = pageManager.create(parentPath, pageName, templatePath, pageTitle);
				Node pageNode = page.adaptTo(Node.class);

				Node jcrNode = null;

				if (page.hasContent()) {
					jcrNode = page.getContentResource().adaptTo(Node.class);
				} else {
					jcrNode = pageNode.addNode("jcr:content", "cq:PageContent");
				}
				jcrNode.setProperty("sling:resourceType", "newsportal/components/page");
				jcrNode.setProperty("jcr:description", description.substring(1, description.length() - 1));
				session.save();
				session.refresh(true);

				logger.info("Page is created....");
			}

		} catch (WCMException | RepositoryException e) {
			logger.info(e.getMessage());
		}

	}

}
