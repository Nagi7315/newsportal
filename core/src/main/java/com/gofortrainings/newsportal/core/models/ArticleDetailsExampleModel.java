package com.gofortrainings.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.gofortrainings.newsportal.core.services.impl.ArticleDetailsImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
//import org.apache.sling.models.annotations.ColorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Locale;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
                    resourceType = "newsportal/components/article-details")
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ArticleDetailsExampleModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleDetailsExampleModel.class);
    @ValueMapValue
    @Default(values = "Test")
    private String articleTitle;

//    @ValueMapValue
//    @ColorValue
//    private String articleTitleColor;

    @ValueMapValue
    private String articleDesc;

    @ValueMapValue(name = "sling:resourceType")
    private String slingResourceType;

    @SlingObject
    ResourceResolver resolver;

    @OSGiService
    ArticleDetailsImpl articleDetailsImpl;

    private String upperCaseTitle;

    private String osgiContent;

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public String getSlingResourceType() {
        return slingResourceType;
    }

    public String getUpperCaseTitle() {
        return upperCaseTitle;
    }

    public String getOsgiContent() {
        return osgiContent;
    }

    @PostConstruct
    public void init(){

        LOGGER.info("In to postconstruct...");

        Resource resource = resolver.getResource("/content/newsportal/us/en");

        Iterator<Resource> childPages = resource.listChildren();

        for (Iterator<Resource> iter = childPages; iter.hasNext(); ) {
            Resource it = iter.next();
            LOGGER.info("name : " + it.getName());
        }

        upperCaseTitle = articleTitle.toUpperCase(Locale.ROOT);
        osgiContent = articleDetailsImpl.test();
    }
}
