package com.gofortrainings.newsportal.core.models;

import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TopNewsModel {

    @ChildResource
    private List<Resource> topNewsCaurosal;

    public List<Resource> getTopNewsCaurosal() {
        return topNewsCaurosal;
    }

}
