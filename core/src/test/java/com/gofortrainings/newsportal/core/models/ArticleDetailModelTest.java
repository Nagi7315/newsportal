package com.gofortrainings.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ArticleDetailModelTest {

    AemContext context = new AemContext();

    ArticleDetailModel articleDetailModel;

    @BeforeEach
    public void setup() {

        context.addModelsForClasses(ArticleDetailModel.class);

        context.load().json("/article-details.json", "/content");
        Resource resource = context.currentResource("/content/article-details");
        articleDetailModel = resource.adaptTo(ArticleDetailModel.class);

//        Map<String,Object> properties = new HashMap<>();
//        properties.put("articleTitle", "Jailer Movie Trailer");
//        properties.put("articleDesc", "Jailer Movie Trailer Desc");
//        properties.put("articleImg", "/content/dam/newsportal/images/article.png");
//        Resource resource = context.create().resource("/content/article-details", properties);
//        articleDetailModel = resource.adaptTo(ArticleDetailModel.class);
    }

    @Test
    void test() {
        assertEquals("Jailer Movie Trailer",articleDetailModel.getArticleTitle());
        assertEquals("Jailer Movie Trailer Desc",articleDetailModel.getArticleDesc());
        assertEquals("/content/dam/newsportal/images/article.png",articleDetailModel.getArticleImg());

    }

}