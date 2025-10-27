package com.gofortrainings.newsportal.core.services;

import java.util.List;

import org.apache.http.NameValuePair;

public interface TghClinicRestService {
	
	public String getMealPlanCuratorQuestions(boolean retry);
	
	public String submitMealPlanCuratorAnswers(List<NameValuePair> data, boolean retry);

}
