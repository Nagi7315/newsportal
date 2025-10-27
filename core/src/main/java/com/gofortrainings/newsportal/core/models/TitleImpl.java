package com.gofortrainings.newsportal.core.models;

import com.adobe.cq.wcm.core.components.models.Title;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class},
       resourceType = "newsportal/components/title",
       adapters = Title.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
       )
public class TitleImpl implements Title {
	
	// child component field
	@ValueMapValue
	private String subTitle;
	
	// creating the Title interface object
	@Self
	@Via(type = ResourceSuperType.class)
	public Title title;
	
	public String getType() {
		return title.getType();
	}
	
	public String getText() {
		return title.getText();
	}
	public String getSubTitle() {
		return subTitle;
	}

}
