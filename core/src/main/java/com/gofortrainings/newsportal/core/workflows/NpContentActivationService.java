package com.gofortrainings.newsportal.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;

@Component(service = WorkflowProcess.class,
        property = "process.label=Np Custom Activation Process"
)
public class NpContentActivationService implements WorkflowProcess {

    @Reference
    Replicator replicator;

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    public static final Logger log = LoggerFactory.getLogger(NpContentActivationService.class);

    @Override
    public void execute(WorkItem item, WorkflowSession wfSession, MetaDataMap args) throws WorkflowException {
        try {
            Session session = wfSession.adaptTo(Session.class);
            String payLoad = item.getWorkflowData().getPayload().toString();
            replicator.replicate(session, ReplicationActionType.ACTIVATE, payLoad);
        } catch (ReplicationException e) {
            log.error("Error : while activating page" + e.getMessage());
        }
    }

}
