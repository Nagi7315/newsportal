package com.gofortrainings.newsportal.core.schedulers;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.gofortrainings.newsportal.core.configuration.ArticleExpiryConfiguration;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = ArticleExpiryConfiguration.class)
public class ArticleExpirySchedulerTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleExpirySchedulerTask.class);

    @Reference
    Scheduler scheduler;

    @Reference
    JobManager jobManager;

    @Activate
    @Modified
    public void activate(ArticleExpiryConfiguration config) {
        if (config.enable()) {
            LOGGER.info("Scheduler is scheduled");
            ScheduleOptions options = scheduler.EXPR(config.schedulerExpression());
            options.name(config.schedulerName());
            options.canRunConcurrently(false);
            scheduler.schedule(this, options);
        } else {
            LOGGER.info("Scheduler is un-schedule");
            scheduler.unschedule(config.schedulerName());
        }
    }

    @Override
    public void run() {
        LOGGER.info("ArticleExpirySchedulerTask is triggered....");

        Map<String, Object> props = new HashMap<>();
        props.put("latest article", "newsportal");
        props.put("path","/content/newsportal/us/en");

        jobManager.addJob("article/job", props);

    }
}
