package com.gofortrainings.newsportal.core.services.impl;

import com.gofortrainings.newsportal.core.configuration.PaymentConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = PaymentImpl.class)
@Designate(ocd = PaymentConfig.class)
public class PaymentImpl {

    String successMSG;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentImpl.class);

    @Activate
    public void activate(PaymentConfig paymentConfig){
        LOGGER.info("Into active method");
        successMSG = paymentConfig.paymentSuccessMessage();

        LOGGER.info("payment msg : " + successMSG);
        LOGGER.info("payment enable : " + paymentConfig.isPayment());
    }

    @Modified
    public void update(PaymentConfig paymentConfig){
        LOGGER.info("Into modified method");
        successMSG = paymentConfig.paymentSuccessMessage();

        LOGGER.info("payment msg : " + successMSG);
        LOGGER.info("payment enable : " + paymentConfig.isPayment());
    }

    public String getPaymentInfo() {
        return successMSG;
    }

    private String paymentInfo;

}
