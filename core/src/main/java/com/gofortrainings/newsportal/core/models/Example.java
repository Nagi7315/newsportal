package com.gofortrainings.newsportal.core.models;

import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Example {
	
	@ValueMapValue
	private String fName;

	@ValueMapValue
	private String lName;

	public String getfName() {
		return fName;
	}

	public String getlName() {
		return lName;
	}

	public String getFullName() {
		return fullName;
	}

	private String fullName;

	@PostConstruct
	public void init(){
	  if(fName != null && lName != null){
	    fullName = fName+lName;
	  }
	}
}
