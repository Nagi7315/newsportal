package com.gofortrainings.newsportal.core.listeners;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.jetbrains.annotations.NotNull;
import org.osgi.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(service = ResourceChangeListener.class,
           property = {
            ResourceChangeListener.CHANGES+"=" + ResourceChangeListener.CHANGE_ADDED,
            ResourceChangeListener.CHANGES+"=" + ResourceChangeListener.CHANGE_REMOVED,
            ResourceChangeListener.CHANGES+"=" + ResourceChangeListener.CHANGE_CHANGED,
            ResourceChangeListener.PATHS + "=/content/country"
        }, immediate = true
)
public class CountryResourceListner implements ResourceChangeListener {

        private static final Logger LOGGER = LoggerFactory.getLogger(CountryResourceListner.class);

        @Override
        public void onChange(@NotNull List<ResourceChange> changes) {
                for(ResourceChange resourceChange : changes ){
                        LOGGER.info("changes : " + changes.toString());
                }
        }
}
