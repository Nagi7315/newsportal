package com.gofortrainings.newsportal.core.listeners;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A services to demonstrate how changes in replication
 * can be listened for. It registers an event handler services.
 * The component is activated immediately after the bundle is
 * started through the immediate flag.
 * Please note, that apart from EventHandler services,
 * the immediate flag should not be set on a services.
 * <p>
 * Event Mechanism in Sling is "Fire event Forget about it" . So there is no guarantee
 * that the event will execute what it needs to if something goes wrong
 * So we have to schedule jobs in such cases
 */
@Component(service = EventHandler.class,
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Demo to listen on changes in the replication",
                EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
        })
public class SimpleResourceListener2 implements EventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    JobManager jobManager;

    public void handleEvent(final Event event) {
        try {
            logger.debug("Resource event: {} at: {}", event.getTopic());
            logger.debug("Replication Event is {}", ReplicationAction.fromEvent(event).getType());
            if (ReplicationAction.fromEvent(event).getType().equals(ReplicationActionType.ACTIVATE)) {
                logger.debug("Triggered activate on {}", ReplicationAction.fromEvent(event).getPath());

                //Create a property map to pass it to the JobConsumer service
                Map<String, Object> jobProperties = new HashMap<String, Object>();
                jobProperties.put("path", ReplicationAction.fromEvent(event).getPath());

                //For some reason if the job fails, but you want to keep retrying ; then in JobConsumer//Set the result as failed . Check the JobConsumer

                jobManager.addJob("sample/replication/job", jobProperties); // This can point to you registered Job Consumer Property Topics

                logger.debug("the  job has been started for: {}", jobProperties);

            }
        } catch (Exception e) {
            logger.error("Exception is ", e);
        }
    }
}
