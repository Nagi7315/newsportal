//package com.gofortrainings.newsportal.core.listeners;
//
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.event.Event;
//import org.osgi.service.event.EventConstants;
//import org.osgi.service.event.EventHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.day.cq.replication.ReplicationAction;
//
//
//@Component(
//        service = EventHandler.class,
//        property = {EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
//              //  EventConstants.EVENT_FILTER + "=(& (type='ACTIVATE')(paths='/content/newsportal/us/en/*'))"
//        }
//)
//public class AticleActivationListener implements EventHandler {
//
//    private static final Logger LOG = LoggerFactory.getLogger(AticleActivationListener.class);
//
//    //  When ever Activate/Deactivate event trigger this method will execute
//    @Override
//    public void handleEvent(Event event) {
//        for (String key : event.getPropertyNames()) {
//            LOG.info("key: {} , value :" + key, event.getProperty(key));
//        }
//
//    }
//
//}
