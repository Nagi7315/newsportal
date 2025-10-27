package com.gofortrainings.newsportal.core.models;

import java.util.List;
import javax.annotation.PostConstruct;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
        adaptables = {SlingHttpServletRequest.class, Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TemplateRenderCondition{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String REQUEST_PARAM_STRING = "item";

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    protected List<String> allowedTemplateTypes;

    @ValueMapValue
    protected String isComponent;

    protected Boolean show = false;

    @PostConstruct
    protected void init() {
        try {
            String suffix = request.getRequestPathInfo().getSuffix();
            String queryParam = request.getParameter(REQUEST_PARAM_STRING);

            for (String allowedTemplateType : allowedTemplateTypes) {
                if(StringUtils.isNotEmpty(suffix) && allowedTemplateType.equals(suffix)) {
                    show = true;
                } else if(StringUtils.isNotEmpty(queryParam)) {
                    Resource pageResource = request.getResourceResolver().getResource(queryParam.concat("/").concat(JcrConstants.JCR_CONTENT));
                    if(pageResource != null) {
                        String templatePath = pageResource.getValueMap().get(NameConstants.NN_TEMPLATE, "");
                        if (templatePath.equals(allowedTemplateType)) {
                            show = true;
                        }
                    }
                } else if(isComponent != null && isComponent.equals("true")) {
                    Resource pageResource = request.getResourceResolver().getResource(suffix.substring(0, suffix.indexOf("/jcr:content")).concat("/").concat(JcrConstants.JCR_CONTENT));
                    if(pageResource != null) {
                        String templatePath = pageResource.getValueMap().get(NameConstants.NN_TEMPLATE, "");
                        if (templatePath.equals(allowedTemplateType)) {
                            show = true;
                        }
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            logger.error("Exception while procession the request: ", e);
        }
        request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(show));
    }
}
