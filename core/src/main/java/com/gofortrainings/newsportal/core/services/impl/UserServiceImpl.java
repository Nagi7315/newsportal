package com.gofortrainings.newsportal.core.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofortrainings.newsportal.core.configuration.UserRestConfiguration;
import com.gofortrainings.newsportal.core.models.UserInfoModel;
import com.gofortrainings.newsportal.core.services.ArticleService;
import com.gofortrainings.newsportal.core.services.UserService;
import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = UserService.class)
@Designate(ocd = UserRestConfiguration.class)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleService.class);
    private String users_API;
    private String usersUpdate_API;

    @Activate
    @Modified
    public void update(UserRestConfiguration config) {
        users_API = config.userRest_API();
        usersUpdate_API = config.usersUpdateRest_API();
    }

    // Fetch data from Rest API
    @Override
    public String getUsers() {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(users_API);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            LOG.error("Error: While reading data from api" + e.getMessage());
        }
        return result;
    }

    @Override
    public void updateUserInfo(UserInfoModel userInfoModel) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(usersUpdate_API);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(userInfoModel);
            StringEntity stringEntity = new StringEntity(requestBody);
            httpPost.setEntity(stringEntity);
            httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<UserInfoModel> getUserList() {

        JsonArray array = JsonParser.parseString(getUsers()).getAsJsonArray();
        Gson gson = new GsonBuilder().create();
        List<UserInfoModel> userInfoModelList = new ArrayList<UserInfoModel>();
        array.forEach(item -> {
            JsonObject jsonObj = JsonParser.parseString(item.toString()).getAsJsonObject();
            UserInfoModel userInfoModel = gson.fromJson(jsonObj, UserInfoModel.class);
            userInfoModelList.add(userInfoModel);
        });
        return userInfoModelList;
    }

}
