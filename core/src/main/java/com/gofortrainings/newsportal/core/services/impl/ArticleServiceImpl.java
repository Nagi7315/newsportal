package com.gofortrainings.newsportal.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import com.gofortrainings.newsportal.core.configuration.ArticleConfiguration;
import com.gofortrainings.newsportal.core.models.UserInfoModel;
import com.gofortrainings.newsportal.core.services.ArticleService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component(service = ArticleService.class)
@Designate(ocd = ArticleConfiguration.class, factory = true)
public class ArticleServiceImpl implements ArticleService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleService.class);
    private String articleApi;

    @Activate
    @Modified
    public void update(ArticleConfiguration config) {
        articleApi = config.articleRestApi();
        LOG.info("ArticleRest API {}", config.articleRestApi(), config.cloneId(),
                config.status());
    }

    // Fetch data from Rest API
    @Override
    public String getArticles() {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(articleApi);
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
        String updateApiUrl = "https://gorest.co.in/public/v2/users/2138834/posts";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(updateApiUrl);
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

        JsonArray array = JsonParser.parseString(getArticles()).getAsJsonArray();
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
