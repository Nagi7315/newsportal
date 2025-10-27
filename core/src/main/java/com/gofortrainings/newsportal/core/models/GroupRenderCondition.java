package com.gofortrainings.newsportal.core.models;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = {SlingHttpServletRequest.class, Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GroupRenderCondition {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    ResourceResolver resolver;

    @ValueMapValue
    private List<String> allowedGroups;

    protected Boolean show = false;

    @PostConstruct
    protected void init() {
        if (resolver != null) {
            UserManager userManager = resolver.adaptTo(UserManager.class);
            if (userManager == null) {
                return;
            }
            boolean belongsToGroup = false;
            try {
                Authorizable currentUser = userManager.getAuthorizable(resolver.getUserID());
                if (currentUser != null) {
                    Iterator<Group> groupsIt = currentUser.memberOf();

                    while (groupsIt.hasNext()) {
                        String groupId = groupsIt.next().getID();
                        for (String groupName : allowedGroups) {
                            if (groupName.contains("/n")) {
                                groupName.replace("/n", "");
                            }
                            if (groupName.equalsIgnoreCase(groupId)) {
                                belongsToGroup = true;
                                break;
                            }
                        }
                    }
                }
            } catch (RepositoryException e) {
                logger.error("Error :", e);
            }
            request.setAttribute(RenderCondition.class.getName(),
                    new SimpleRenderCondition(belongsToGroup));
        }
    }
}
