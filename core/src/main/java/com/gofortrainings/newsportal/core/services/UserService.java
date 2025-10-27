package com.gofortrainings.newsportal.core.services;

import com.gofortrainings.newsportal.core.models.UserInfoModel;

import java.util.List;

public interface UserService {

    public String getUsers();

    public List<UserInfoModel> getUserList();

    public void updateUserInfo(UserInfoModel userInfoModel);
}
