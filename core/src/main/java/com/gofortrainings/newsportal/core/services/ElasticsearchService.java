package com.gofortrainings.newsportal.core.services;

import org.apache.sling.api.resource.Resource;

import java.io.IOException;

public interface ElasticsearchService {

    // This method sends the JSON response
    public int sendEvent(String jsonBody, String topic, Resource resource, String actionType) throws IOException;
}
