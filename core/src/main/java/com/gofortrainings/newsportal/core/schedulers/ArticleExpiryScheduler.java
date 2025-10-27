package com.gofortrainings.newsportal.core.schedulers;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import javax.jcr.Session;

import com.gofortrainings.newsportal.core.configuration.ArticleExpiryConfiguration;
import com.gofortrainings.newsportal.core.utils.Constants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;


// In this approach we can't stop our scheduler
//@Component(service = Runnable.class,
//            property = {
//        		"scheduler.expression = */5 * * ? * *"
//           },
//           immediate = true
//          )
// Scheduler is used to trigger an activity at particular or period of time
// Scheduler created with the help of Scheduler OSGI service using " @Reference Scheduler scheduler ";
@Component(service = Runnable.class, immediate = true)
@Designate(ocd = ArticleExpiryConfiguration.class)
public class ArticleExpiryScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleExpiryScheduler.class);

    @Reference
    Replicator replicator;

    @Reference
    Scheduler scheduler;

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    @Activate
    @Modified
    public void update(ArticleExpiryConfiguration config) {

        schedule(config);
    }

    public void schedule(ArticleExpiryConfiguration config) {
        if (config.enable()) {
            ScheduleOptions options = scheduler.EXPR(config.schedulerExpression());
            options.name(config.schedulerName());
            options.canRunConcurrently(false);
            scheduler.schedule(this, options);
        } else {
            scheduler.unschedule(config.schedulerName());
        }
    }

    @Override
    public void run() {
        try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            if(pageManager != null){
                Page articlePage = pageManager.getPage("/content/newsportal/us/en/article-1");
                Iterator<Page> childPages = articlePage.listChildren();
                while (childPages.hasNext()) {
                    Page page = childPages.next();
                    Resource contentResource = page.getContentResource();
                    ValueMap properties = contentResource.getValueMap();
                    Date articleExpiry = null;
                    if (properties.containsKey("articleExpiry")) {
                         articleExpiry = properties.get("articleExpiry", Date.class);
                    }
                    Date today = new Date();
                    if (articleExpiry != null && articleExpiry.compareTo(today) < 0) {
                        Session session = resolver.adaptTo(Session.class);
                        replicator.replicate(session, ReplicationActionType.DEACTIVATE, page.getPath());
                    }
                }
            }

        } catch (LoginException | ReplicationException e) {
            e.printStackTrace();
        }
    }


}
