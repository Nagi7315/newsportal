package com.gofortrainings.newsportal.core.listeners;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.PageEvent;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Component(service = EventHandler.class,
        property = {
        EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC
        }
)
public class SitePageCreationListener implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(SitePageCreationListener.class);

    @Reference
    SlingSettingsService slingSettingsService;

    @Override
    public void handleEvent(Event event) {

        if(slingSettingsService.getRunModes().contains(Externalizer.PUBLISH)){
            return;
        }

        List<Map<String,Object>> props = (List<Map<String, Object>>) event.getProperty("modifications");
        for(Map data : props){
            String path = data.get("path").toString().concat("/jcr:content");
            String eventType = data.get("type").toString();
            if(path.startsWith("/content/newsportal") || path.startsWith("/content/experience-fragments/newsportal") || path.endsWith("/jcr:content")){

            }

        }

        /*PageEvent pageEvent = PageEvent.fromEvent(event);
        if (null == pageEvent) {
            return;
        }
        Iterator<PageModification> modifications = pageEvent.getModifications();
        while (modifications.hasNext()) {
            PageModification modification = modifications.next();
            String modificationPath = modification.getPath();
            PageModification.ModificationType type = modification.getType();
            if (type == PageModification.ModificationType.CREATED) {
                log.info("Page created {}", modificationPath);
            } else if (type == PageModification.ModificationType.MODIFIED) {
                log.info("Page modified {}", modificationPath);
            }
        }  */
    }
}
