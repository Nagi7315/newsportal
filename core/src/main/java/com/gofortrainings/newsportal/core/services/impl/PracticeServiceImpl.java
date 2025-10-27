package com.gofortrainings.newsportal.core.services.impl;

import com.gofortrainings.newsportal.core.configuration.ArticleConfiguration;
import com.gofortrainings.newsportal.core.configuration.PracticeConfiguration;
import com.gofortrainings.newsportal.core.services.PracticeService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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

@Component(service = PracticeService.class)
@Designate(ocd= PracticeConfiguration.class)
public class PracticeServiceImpl implements PracticeService {

    private static final Logger LOG = LoggerFactory.getLogger(PracticeServiceImpl.class);
    private String restApi;

    @Activate
    @Modified
    public void update(ArticleConfiguration config) {
        restApi = config.articleRestApi();

    }
    @Override
    public String getApiData() {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(restApi);
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
}
