package com.gofortrainings.newsportal.core.listeners;

import com.day.cq.replication.*;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.NewsportalServiceUtils;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.distribution.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Component(immediate = true, service = EventHandler.class,
        property = {
        EventConstants.EVENT_TOPIC+"="+ReplicationAction.EVENT_TOPIC
})
public class InvalidateCacheEventHandler implements EventHandler {
    @Reference
    ResourceResolverUtil resourceResolverUtil;

    // Distributor will help us to invalidate cache
    @Reference
    private Distributor distributor;

    private static final Logger LOG = LoggerFactory.getLogger(InvalidateCacheEventHandler.class);

    // Servlet path going to invalidate
    private static final String servletPath  =  "/content/practice/ea/en/data/product.products.json";

    public void handleEvent(Event event) {

        ReplicationAction replicationAction = ReplicationAction.fromEvent(event);
        ReplicationActionType replicationType = replicationAction.getType();

        String agent = getAgentFromReplicationEvent(event);

        LOG.info("Agent from event : {}", agent);

        // Flush or invalidate cache if replication type is activate
        if (replicationType.equals(ReplicationActionType.ACTIVATE)) {

            try (ResourceResolver flushingResourceResolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {

                // Invalidate cache
                invalidateCache(Arrays.asList(servletPath), agent,
                        DistributionRequestType.INVALIDATE, Boolean.TRUE, flushingResourceResolver);
            } catch (Exception e) {
                LOG.error("Replication exception occurred during Dispatcher Flush request.", e);
            }
        }
    }

    // Function will help us to get agent
    private String getAgentFromReplicationEvent(Event event) {
        if (Objects.nonNull(event)) {
            List agentIds = (List) event.getProperty("agentIds");
            String agent = (String) agentIds.get(0);
            return Objects.nonNull(agent) ? agent : "publish";
        }
        return "publish";
    }

    private void invalidateCache(List<String> pathsToInvalidate, String agent,
                                 DistributionRequestType requestType, Boolean isDeep, ResourceResolver resolver) {
        // Invalidate cache for specific paths
        DistributionRequest distributionRequest = new SimpleDistributionRequest(requestType,
                isDeep, pathsToInvalidate.toArray(new String[0]));
        if (!pathsToInvalidate.isEmpty()) {

            // Invalidate cache using DistributionRequest
            DistributionResponse distributionResponse = distributor.distribute(agent,
                    resolver, distributionRequest);
            LOG.debug("Distribution Response: {}", distributionResponse);
            LOG.debug("Distribution message: {}", distributionResponse.getMessage());
        }
    }
}
