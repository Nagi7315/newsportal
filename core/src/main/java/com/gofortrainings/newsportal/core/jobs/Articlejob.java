package com.gofortrainings.newsportal.core.jobs;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

@Component(service = JobConsumer.class, immediate = true,
            property = {
            JobConsumer.PROPERTY_TOPICS+"=article/job",
                    Constants.SERVICE_DESCRIPTION + "= Article Expiry"
            }
)
public class Articlejob implements JobConsumer {

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(Articlejob.class);

    @Override
    public JobResult process(Job job) {
        try {
            LOGGER.error("Job is triggered");
            try(ResourceResolver resolver = resourceResolverUtil.getResolver(com.gofortrainings.newsportal.core.utils.Constants.VEERA_SERVICE_USER)) {
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                String pagePath = job.getProperty("path", String.class);
                if (pageManager != null) {
                    Page parentPage = pageManager.getPage(pagePath);

                    Iterator<Page> childPage = parentPage.listChildren();

                    while (childPage.hasNext()) {
                        Page page = childPage.next();
                        Resource resource = page.getContentResource();
                        ValueMap properties = resource.adaptTo(ValueMap.class);
                        String featureArticle = properties.get("feature-article", String.class);
                        if (featureArticle == null) {
                            ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
                            modifiableValueMap.put("feature-article", "newsportal-article");
                            resource.getResourceResolver().commit();
                        }
                    }
                }
            }catch (Exception e){
                LOGGER.info("Error in ArticleExpirySchedulerTask : " + e.getMessage());
            }
            return JobResult.OK;
        }catch (Exception e){
            LOGGER.info("Error in Article Job : " + e.getMessage());
            return JobResult.FAILED;
        }
    }
}
