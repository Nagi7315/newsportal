package com.gofortrainings.newsportal.core.services;

import java.util.List;
import com.gofortrainings.newsportal.core.models.UserInfoModel;

public interface ArticleService {
	
	public String getArticles();

	public List<UserInfoModel> getUserList();

	public void updateUserInfo(UserInfoModel userInfoModel);
}
