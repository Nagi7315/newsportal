package com.gofortrainings.newsportal.core.filters;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@SlingServletFilter(scope = {SlingServletFilterScope.REQUEST},
        pattern = "/content/dam/newsportal/.*")
public class NPAssetFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(NPAssetFilter.class);

    @Reference
    private SlingSettingsService slingSettingsService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(slingSettingsService.getRunModes().contains("author")){
            return;
        }
        SlingHttpServletRequest slingHttpServletRequest = (SlingHttpServletRequest) request;
        try (ResourceResolver resolver = slingHttpServletRequest.getResourceResolver()) {
            String assetPath = slingHttpServletRequest.getResource().getPath();
            if (StringUtils.isNotBlank(assetPath)) {

                Resource metadataResource = resolver.getResource(assetPath + "/jcr:content/metadata");
                SlingHttpServletResponse SlingHttpServletResponse = (SlingHttpServletResponse) response;
                List<String> tagNames = getTagName(metadataResource);
                Session session = resolver.adaptTo(Session.class);
                String userID = session.getUserID();
                List<String> groupNames = getUserGroupNames(userID, resolver.adaptTo(UserManager.class));
                boolean isValidUser = compareGroupWithTagNames(tagNames, groupNames);

                if (isValidUser) {
                    SlingHttpServletResponse.setContentType("image/png");
                    SlingHttpServletResponse.setHeader("Content-Disposition", "attachment");
                } else {
                    SlingHttpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized-user");
                }

            } else {
                log.debug("Asset path is null");
            }
        }
    }

    private boolean compareGroupWithTagNames(List<String> tagNames, List<String> groupNames) {
        boolean isValidUser = false;
        if (groupNames == null || tagNames == null) {
            return false;
        }
        for (String groupName : groupNames) {
            if (tagNames.contains(groupName)) {
                isValidUser = true;
                break;
            }
        }
        return isValidUser;
    }

    private List<String> getTagName(Resource metadataResource) {
        List<String> tagNames = new ArrayList<>();
        if (metadataResource != null) {
            ValueMap props = metadataResource.getValueMap();

            if (props.containsKey("cq:tags")) {
                String[] tags = props.get("cq:tags", String[].class);

                for (String tag : tags) {
                    String[] tagItems = tag.split("/");
                    if (tagItems.length > 0) {
                        tagNames.add(tagItems[1]);
                    }
                }
            } else {
                log.debug("cq:tags property is null");
            }
        }
        return tagNames;
    }

    private List<String> getUserGroupNames(String userId, UserManager userManager) {
        List<String> groupNames = new ArrayList<>();
        try {
            Iterator<Group> group = userManager.getAuthorizable(userId).memberOf();
            while (group.hasNext()) {
                groupNames.add(group.next().getPrincipal().getName());
            }
        } catch (RepositoryException e) {
            log.error("Error : While getting the User Manager");
        }
        return groupNames;
    }

    @Override
    public void destroy() {

    }
}
