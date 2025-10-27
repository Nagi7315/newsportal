package com.gofortrainings.newsportal.core.listeners;

import com.day.cq.replication.ReplicationAction;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(service = EventHandler.class,
        property =
                {
                        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
                },
        immediate = true)
public class ElasticsearchEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchEventHandler.class);

    @Reference
    private ResourceResolverUtil resourceResolverUtil;

    @Reference
    private JobManager jobManager;


    @Override
    public void handleEvent(Event event) {
        LOG.debug("handleEvent : starts...");

        String topic = event.getTopic();
        LOG.debug("handleEvent : topic {}", topic);

        ReplicationAction actionType = ReplicationAction.fromEvent(event);

        String pagePath = actionType.getPath();
        LOG.debug("handleEvent : paths {}", pagePath);

        String action = actionType.getType().getName().toUpperCase();
        LOG.debug("handleEvent : type {}", action);

        if (StringUtils.isNotBlank(pagePath) && StringUtils.contains(pagePath, "/content/newsportal") && StringUtils.isNotBlank(action)) {
            addJob(action, topic, pagePath);
        } else {
            LOG.debug("Page is Empty");
        }

    }

    private void addJob(String action, String topic, String path) {

        // create job properties
        Map<String, Object> jobProperties = new HashMap<>();
        jobProperties.put("path", path);
        jobProperties.put("topic", topic);
        jobProperties.put("actionType", action);

        // add to job in queue
        jobManager.addJob("com/elastic-search/JobHandler", jobProperties);
    }
}
