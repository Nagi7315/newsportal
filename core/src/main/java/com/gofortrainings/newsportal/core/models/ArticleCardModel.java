package com.gofortrainings.newsportal.core.models;

import com.gofortrainings.newsportal.core.utils.NewsportalServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ArticleCardModel {

    @ValueMapValue
    private String articlePage;

    @ValueMapValue
    @Default(intValues = 2)
    private int articles;

    @SlingObject
    ResourceResolver resolver;

    @OSGiService
    NewsportalServiceUtils newsportalServiceUtils;

    private List<ArticleDetailModel> articleList;

    @PostConstruct
    public void init(){
        if (StringUtils.isNotBlank(articlePage)){
            articleList = new ArrayList<>();
            String query = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE(["+articlePage+"]) ORDER BY s.[jcr:content/cq:lastModified] DESC option(limit "+articles+")";
            Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);
            while(result.hasNext()){
                Resource articleResource = newsportalServiceUtils.getComponentResource(result.next(), "newsportal/components/article-details");
                if(articleResource != null) {
                    ArticleDetailModel articleDetailModel = articleResource.adaptTo(ArticleDetailModel.class);
                    articleList.add(articleDetailModel);
                }
            }
        }

    }

    public List<ArticleDetailModel> getArticleList() {
        return articleList;
    }
}
