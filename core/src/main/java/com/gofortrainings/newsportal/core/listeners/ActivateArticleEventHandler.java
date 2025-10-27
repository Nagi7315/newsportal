package com.gofortrainings.newsportal.core.listeners;

import com.day.cq.replication.ReplicationAction;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EventHandler.class,
        property = {
              //  EventConstants.EVENT_TOPIC+"="+ ReplicationAction.EVENT_TOPIC,
              //  EventConstants.EVENT_FILTER+"= (& (type=ACTIVATE) (paths=/content/newsportal/us/en/*))"
        },
        immediate = true
)
public class ActivateArticleEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivateArticleEventHandler.class);
    @Reference
    ResourceResolverUtil resolverUtil;

    @Override
    public void handleEvent(Event event) {
        LOGGER.error("ActivateArticleEventHandler is activated");
        try {
            ResourceResolver resolver = resolverUtil.getResolver(Constants.VEERA_SERVICE_USER);

            String[] paths = (String[]) event.getProperty("paths");
            for(String path : paths){
                Resource resource = resolver.getResource(path + "/jcr:content");
                ModifiableValueMap valueMap = resource.adaptTo(ModifiableValueMap.class);
                valueMap.put("pageReplicated","true");
                resource.getResourceResolver().commit();
            }
        } catch (LoginException | PersistenceException e) {
            throw new RuntimeException(e);
        }
    }
}
