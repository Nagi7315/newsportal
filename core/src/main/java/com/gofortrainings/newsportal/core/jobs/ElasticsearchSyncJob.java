package com.gofortrainings.newsportal.core.jobs;

import com.gofortrainings.newsportal.core.services.ElasticsearchService;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.NewsportalServiceUtils;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(service = {ElasticsearchSyncJob.class, JobConsumer.class}, property = {
        JobConsumer.PROPERTY_TOPICS + "=com/elastic-search/JobHandler"}, immediate = true)
public class ElasticsearchSyncJob implements JobConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchSyncJob.class);

    @Reference
    NewsportalServiceUtils newsportalServiceUtils;

    @Reference
    ElasticsearchService elasticsearchService;

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    @Override
    public JobResult process(Job job) {
        // pull properties
        String path = job.getProperty("path").toString();
        String topic = job.getProperty("topic").toString();
        String actionType = job.getProperty("actionType").toString();
        LOG.info("Path:::: {} ActionType:::: {} Topic:::: {}", path, actionType, topic);

        try(ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            Resource resource = resolver.getResource(path);
            boolean resourceExists = null != resource && !ResourceUtil.isNonExistingResource(resource);
            if (resourceExists) {
                String jsonBody ;

               // if (ReplicationActionType.ACTIVATE.getName().equalsIgnoreCase(actionType)) {
                    JsonObject jsonObjectData = generatePage(resource);
                    jsonBody = newsportalServiceUtils.json2String(jsonObjectData);

                    if (StringUtils.isNotBlank(jsonBody)) {
                        // send event
                        try {
                            sendEvent(jsonBody, topic, resource, actionType);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
               // }
            }

        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void sendEvent(String jsonBody, String topic, Resource resource, String actionType) throws IOException {
        int httpStatus = elasticsearchService.sendEvent(jsonBody, topic, resource, actionType);
        if (httpStatus != 200) {
            throw new IOException("Error sending page :: httpStatus :: "+httpStatus);
        }
    }
    // Create JSON or document for syncing
    private JsonObject generatePage(Resource resource) {
        JsonObject document = new JsonObject();
        Resource contentresource = resource.getChild("jcr:content");
        if (contentresource != null) {
            ValueMap vm = contentresource.getValueMap();
            document.addProperty("title", vm.get("jcr:title", String.class));
            document.addProperty("description", vm.get("jcr:description", String.class));
            document.addProperty("path", resource.getPath());
            document.addProperty("name", resource.getName());
        }
        return document;
    }
}
