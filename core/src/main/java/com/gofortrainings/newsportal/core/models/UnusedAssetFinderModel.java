package com.gofortrainings.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class UnusedAssetFinderModel {

    @ValueMapValue
    private String contentPath;

    @ValueMapValue
    private String assetFolderPath;

    @ScriptVariable
    private Resource resource;

//    private
//

    public String getContentPath() {
        return contentPath;
    }

    public String getAssetFolderPath() {
        return assetFolderPath;
    }

    public Resource getResource() {
        return resource;
    }
//    @PostConstruct
//    public void init(){
//
//    }
}
