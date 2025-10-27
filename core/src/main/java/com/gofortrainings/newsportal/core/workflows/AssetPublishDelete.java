package com.gofortrainings.newsportal.core.workflows;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.gofortrainings.newsportal.core.services.AssetManagementService;
import com.gofortrainings.newsportal.core.services.FileService;

@Component(service = WorkflowProcess.class,
property = {
		   "process.label"+" = Asset Publish and Delete Process"
},
immediate = true)
public class AssetPublishDelete implements WorkflowProcess{
	
	private static final Logger log = LoggerFactory.getLogger(AssetPublishDelete.class);
  
	@Reference
	AssetManagementService assetManagementService;
	
	@Reference
	FileService fileService;
	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		//String payload = item.getWorkflowData().getPayload().toString();
		String payload = fileService.getCsvFilePath();
		log.info("Payload"+payload);
		assetManagementService.processAssets(payload);
		log.info("End of Workflow");
	}
	

}
