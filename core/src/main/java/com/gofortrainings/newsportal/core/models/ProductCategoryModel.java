package com.gofortrainings.newsportal.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = {Resource.class,
        SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCategoryModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    @Default(values = "news-portal:categories")
    private String categoryTag;

    @SlingObject
    ResourceResolver resolver;
    List<ProductCategoryInfo> productCategoryInfoList;

    @PostConstruct
    public void init() {
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        if (tagManager != null) {
            productCategoryInfoList = new ArrayList<>();
            Tag tagObj = tagManager.resolve(categoryTag);
            Iterator<Tag> childTags = tagObj.listChildren();
            ProductCategoryInfo productCategoryInfo = null;
            while (childTags.hasNext()) {
                Tag tag = childTags.next();
                String query = "SELECT * FROM [dam:Asset] AS s WHERE ISDESCENDANTNODE([/content/dam/newsportal]) and s.[jcr:content/cq:tags] like '"
                        + tag + "%'";
                Iterator<Resource> resourceIterator = resolver.findResources(query, Query.JCR_SQL2);
                int count = 0;
                String imagePath = null;
                while (resourceIterator.hasNext()) {
                    if (count == 1) {
                        imagePath = getImagePath(resourceIterator.next());
                    }
                    count++;
                }
                productCategoryInfo = new ProductCategoryInfo();
                productCategoryInfo.setCategoryProductTitle(tag.getTitle());
                productCategoryInfo.setCategoryProductCount(count);
                productCategoryInfo.setCategoryProductImage(imagePath);
            }
            productCategoryInfoList.add(productCategoryInfo);
        }
    }

    private String getImagePath(Resource resource){
        String imagePath = null;
        if (resource != null){
            imagePath = resource.getPath();
        }
        return imagePath;
    }
}
