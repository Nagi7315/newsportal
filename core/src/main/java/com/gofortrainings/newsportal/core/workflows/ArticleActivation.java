package com.gofortrainings.newsportal.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;

@Component(service = WorkflowProcess.class, immediate = true,
        property = {"process.label=Article Activation"}
)
public class ArticleActivation implements WorkflowProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleActivation.class);

    @Reference
    Replicator replicator;

    @Override
    public void execute(WorkItem item, WorkflowSession wfSession, MetaDataMap args) throws WorkflowException {

        String payLoad = item.getWorkflowData().getPayload().toString();
        Session session = wfSession.adaptTo(Session.class);
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, payLoad);
        } catch (ReplicationException e) {
            throw new RuntimeException(e);
        }

      //  LOGGER.error("Article payload : " + payLoad);

    }

}
