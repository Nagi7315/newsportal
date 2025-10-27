package com.gofortrainings.newsportal.core.listeners;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.day.cq.dam.api.DamEvent;
import com.adobe.granite.workflow.WorkflowSession;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component(service = EventHandler.class,
        property = {EventConstants.EVENT_TOPIC + "=" + DamEvent.EVENT_TOPIC
        }
)
public class SiteAssetCreationListener implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(SiteAssetCreationListener.class);

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    @Override
    public void handleEvent(Event event) {
        try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            String assetPath = event.getProperty(Constants.ASSET_PATH).toString();
            String eventType = event.getProperty("type").toString();
            if (eventType.equals("ASSET_CREATED") && assetPath.endsWith(Constants.PDF_EXTENSION)) {
                Resource resource = resolver.getResource(assetPath.concat(Constants.METADATA));
                if (resource != null) {
                    ModifiableValueMap metaProps = resource.adaptTo(ModifiableValueMap.class);
                    if (metaProps != null) {
                        String uniqueID = UUID.randomUUID().toString();
                        metaProps.put(Constants.ASSET_ID_2, uniqueID);
                        resolver.commit();
                    }
                }
            }
            // https://medium.com/@manumathew28.94/understanding-aem-workflow-b017f0519c44
            else if (eventType.equals("METADATA_UPDATED") && assetPath.endsWith(Constants.PDF_EXTENSION)) {
                WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class);
                if (workflowSession != null) {
                    WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/np-page-activation");
                    WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", assetPath);
                    Map<String, Object> workflowMetaData = new HashMap<>();
                    workflowSession.startWorkflow(workflowModel, workflowData, workflowMetaData);
                }
            }
        } catch (LoginException | PersistenceException | WorkflowException e) {
            log.error("Error : while activating page" + e.getMessage());
        }
    }
}
