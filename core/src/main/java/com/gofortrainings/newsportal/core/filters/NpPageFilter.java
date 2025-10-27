package com.gofortrainings.newsportal.core.filters;

import com.gofortrainings.newsportal.core.configuration.NpPageFilterConfiguration;
import com.gofortrainings.newsportal.core.preprocessor.ReplicationPreprocessor;
import com.gofortrainings.newsportal.core.utils.ResourceResolverUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

/* 1. write a filter to execute on article pages to check articleTag and articleExpiry in page properties.
   2. Redirect to home page if any article doesn't have articleTag and articleExpiry property in page level.
   3. This filter should execute only on publish
Note: don't use slingsettingservice to identify run modes, use configurationPolicy - Required in @Component and create a dummy configuration in config.publish to make this activate only on publish server.
*/

@Component(service = Filter.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@SlingServletFilter(scope = {SlingServletFilterScope.REQUEST},
        pattern = "/content/newsportal/.*")
@Designate(ocd = NpPageFilterConfiguration.class)
public class NpPageFilter implements Filter {

    private String pageProperty;

    @Reference
    ResourceResolverUtil resourceResolverUtil;

    private static final Logger log = LoggerFactory.getLogger(ReplicationPreprocessor.class);

    @Activate
    @Modified
    public void update(NpPageFilterConfiguration config) {
        this.pageProperty = config.pageProperty();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SlingHttpServletRequest slingHttpServletRequest = (SlingHttpServletRequest) request;
        SlingHttpServletResponse slingHttpServletResponse = (SlingHttpServletResponse) response;

        try (ResourceResolver resolver = slingHttpServletRequest.getResourceResolver()) {
            String pagePath = slingHttpServletRequest.getResource().getPath();
            if (StringUtils.isNotBlank(pagePath)) {
                Resource contentResource = resolver.getResource(pagePath + "/jcr:content");
                if (contentResource != null) {
                    ValueMap props = contentResource.getValueMap();
                    String[] tags = props.get(pageProperty, String[].class);
                    Date articleExpiry = props.get("articleExpiry", Date.class);
                    String articleTag = findArticleTag(tags);

                    if (StringUtils.isBlank(articleTag) || articleExpiry == null) {
                        slingHttpServletResponse.sendRedirect("/content/newsportal/us/en.html");
                    }
                }
            }
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

    @Override
    public void destroy() {

    }
}
