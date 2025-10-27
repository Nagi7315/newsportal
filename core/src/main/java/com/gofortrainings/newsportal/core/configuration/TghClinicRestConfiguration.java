package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface TghClinicRestConfiguration {
	
	@AttributeDefinition (name = "TGH Clinic Rest API Domain")
	public String tghClinicRestApiDomain() default "https://api.coach-dev.tghclinic.com";
	
	@AttributeDefinition (name = "Meal Plan Question API")
	public String mealPlanQuestionApi() default "/api/auth/get_itc_meal_plan_question_answer";
	
	@AttributeDefinition (name = "Meal Plan Answer API")
	public String mealAnswerApi() default "/api/auth/give_meal_plan_anawer";
}
