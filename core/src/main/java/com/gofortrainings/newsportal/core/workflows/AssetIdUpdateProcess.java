package com.gofortrainings.newsportal.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Component(service = WorkflowProcess.class,
        property = "process.label=Asset Id Update Process"
)
public class AssetIdUpdateProcess implements WorkflowProcess {

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    public static final Logger log = LoggerFactory.getLogger(AssetIdUpdateProcess.class);

    @Override
    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            String payLoad = item.getWorkflowData().getPayload().toString();
            if(payLoad.endsWith(Constants.PDF_EXTENSION)){
                Resource resource = resolver.getResource(payLoad.concat(Constants.METADATA));
                if (resource != null) {
                    ModifiableValueMap metaProps = resource.adaptTo(ModifiableValueMap.class);
                    if (metaProps != null) {
                        String uniqueID = UUID.randomUUID().toString();
                        metaProps.put(Constants.ASSET_ID, uniqueID);
                        resolver.commit();
                    }
                }
            }
        } catch (LoginException | PersistenceException e) {
            log.error("Error : while activating page" + e.getMessage());
        }
    }
}
