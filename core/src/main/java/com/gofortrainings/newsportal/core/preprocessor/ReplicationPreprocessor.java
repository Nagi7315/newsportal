package com.gofortrainings.newsportal.core.preprocessor;

import com.day.cq.replication.*;
import com.gofortrainings.newsportal.core.utils.Constants;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

//Implement preprocessor to execute before activation of page to cross-check articleTag and articleExpiry properties, it should return ReplicationException if two properties are not available and articleExpiry should greater than current date
//preprocessor will execute in author server
@Component(service = Preprocessor.class,
        property = {EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
                EventConstants.EVENT_FILTER + "=(& (type='ACTIVATE')(paths='/content/newsportal/us/en/*'))"}
)
public class ReplicationPreprocessor implements Preprocessor {

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    private static final Logger log = LoggerFactory.getLogger(ReplicationPreprocessor.class);

    @Override
    public void preprocess(ReplicationAction action, ReplicationOptions options) throws ReplicationException {

//        if (action == null || !ReplicationActionType.ACTIVATE.equals(action.getType())) {
//            return;
//        }

        if (action == null || action.getType().equals(ReplicationActionType.DEACTIVATE) ) {
            return;
        }

        try (ResourceResolver resolver = resourceResolverUtil.getResolver(Constants.VEERA_SERVICE_USER)) {
            String pagePath = action.getPath();
            if (StringUtils.isNotBlank(pagePath)) {
                Resource contentResource = resolver.getResource(pagePath + "/jcr:content");
                if (contentResource != null) {
                    ValueMap props = contentResource.getValueMap();
                    String[] tags = props.get("cq:tags", String[].class);
                    String articleTag = findArticleTag(tags);
                    Date articleExpiry = props.get("articleExpiry", Date.class);
                    Date date = new Date();
                    if (StringUtils.isBlank(articleTag) || articleExpiry == null || articleExpiry.compareTo(date) < 0) {
                        //  throw new RuntimeException("Article Tag is Empty or Article Expiry is Empty or Article Expired");
                    }

                } else {
                    log.debug("Page content resource is null");
                }
            } else {
                log.debug("Page path is empty");
            }
        } catch (LoginException e) {
            log.error("Error : while getting the Resource Resolver");
        }
    }

    private String findArticleTag(String[] tags) {
        if (tags != null) {
            for (String tag : tags) {
                if (tag.startsWith("newsportal:categories")) {
                    String[] tagItems = tag.split("/");
                    if (tagItems.length >= 2) {
                        return tagItems[1];
                    }
                }
            }
        }
        return null;
    }
}
