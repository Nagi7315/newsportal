package com.gofortrainings.newsportal.core.services.impl;

import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ArticleDetailsImpl.class, immediate = true)
public class ArticleDetailsImpl {

    @Reference
    PaymentImpl payment;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleDetailsImpl.class);

    @Activate
    public void activate(){
        LOGGER.info("Bundle is active : " + payment.getPaymentInfo());

        LOGGER.error("Bundle is actived with error log");
    }

    @Modified
    public void modified(){
        LOGGER.info("Bundle is modified");

        LOGGER.error("Bundle is modified with error log");
    }

    @Deactivate
    public void deActivate(){
        LOGGER.info("Bundle is de active");

        LOGGER.error("Bundle is deActivate with error log");
    }

    public String test(){
        return "This is from OSGI Service";
    }


}
