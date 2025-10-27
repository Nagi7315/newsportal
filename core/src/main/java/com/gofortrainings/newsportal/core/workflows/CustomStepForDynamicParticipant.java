package com.gofortrainings.newsportal.core.workflows;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(service = ParticipantStepChooser.class,
           property = {
		                "chooser.label"+" = Workflow Participant Chooser"
                      },
                       immediate = true)
public class CustomStepForDynamicParticipant  implements ParticipantStepChooser{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomStepForDynamicParticipant.class);

	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
			throws WorkflowException {
		logger.info("################ Inside the SampleProcessStepChooserImpl GetParticipant #########################");
	    String participant = "admin";
	    Workflow workflow = workItem.getWorkflow();
	    String initiator = workflow.getInitiator();
	    List<HistoryItem> wfHistory = workflowSession.getHistory(workflow);
	    if (!wfHistory.isEmpty()) {
	      participant = initiator;
	    } else {
	      participant = "admin";
	    }
	    logger.info("####### Participant : " + participant + " ##############");
	    return participant;		
	}

}
