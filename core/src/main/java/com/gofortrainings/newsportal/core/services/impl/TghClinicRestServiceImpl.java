package com.gofortrainings.newsportal.core.services.impl;

import java.util.List;

import org.apache.http.NameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import com.gofortrainings.newsportal.core.configuration.TghClinicRestConfiguration;
import com.gofortrainings.newsportal.core.services.TghClinicRestService;

@Component(service = TghClinicRestService.class)
@Designate(ocd = TghClinicRestConfiguration.class)
public class TghClinicRestServiceImpl implements TghClinicRestService{
	
	TghClinicRestConfiguration config;

	@Override
	public String getMealPlanCuratorQuestions(boolean retry) {		
		return null;
	}

	@Override
	public String submitMealPlanCuratorAnswers(List<NameValuePair> data, boolean retry) {		
		return null;
	}
	
	@Activate
	@Modified
	public void activate(TghClinicRestConfiguration config) {
		 config = this.config;		
	}

}
