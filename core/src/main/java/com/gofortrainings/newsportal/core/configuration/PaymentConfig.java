package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Payment Services", description = "This config is to define payment related values")
public @interface PaymentConfig {

    @AttributeDefinition(name = "Success Message", type = AttributeType.STRING)
    public String paymentSuccessMessage() default "Payment is successful...!";

    @AttributeDefinition(name = "isPayment", type = AttributeType.BOOLEAN)
    public boolean isPayment();

}
