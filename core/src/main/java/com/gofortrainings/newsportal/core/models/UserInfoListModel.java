package com.gofortrainings.newsportal.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import com.gofortrainings.newsportal.core.services.UserService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UserInfoListModel {

	@ValueMapValue
	private String title;

	@OSGiService
	UserService userService;

	private List<UserInfoModel> userInfoModelList;

	public String getTitle() {
		return title;
	}
	public List<UserInfoModel> getUserInfoList() {
		return userService.getUserList();
	}

}
