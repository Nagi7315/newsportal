package com.gofortrainings.newsportal.core.models;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.resource.Resource;

import javax.annotation.PostConstruct;
import java.util.Date;

@Model(adaptables = Resource.class)
public class ArticleDetailsPracticeModel {

    @ValueMapValue
    private String articleTitle;

    @ValueMapValue
    private String articleDes;

    @ValueMapValue
    private String articleImage;

    @ValueMapValue
    private Date articleExpiry;

    @PostConstruct
    public void init() {
        if(articleExpiry != null) {
            Date today = new Date();
            if(articleExpiry.compareTo(today)<0){
                boolean articleIsExpired = true;
            }
        }
    }


    public String getArticleTitle() {
        return articleTitle;
    }
    public String getArticleDes() {
        return articleDes;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public Date getArticleExpiry() {
        return articleExpiry;
    }
}
