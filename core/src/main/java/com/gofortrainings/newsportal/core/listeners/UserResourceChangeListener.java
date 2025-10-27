package com.gofortrainings.newsportal.core.listeners;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service =ResourceChangeListener.class,
      //To listen the Resource(Node) Event
           property = {
        		   ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_ADDED,
        		   ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_CHANGED,
        		   ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_REMOVED,
        		   ResourceChangeListener.PATHS+"=/content/user-details"
           }
		)
public class UserResourceChangeListener implements ResourceChangeListener{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserResourceChangeListener.class);

//  When ever Resource(Node) Event trigger this method will execute
	@Override
	public void onChange(List<ResourceChange> changes) {
		LOG.info("Resource Added/removed/updated");
	}
}
