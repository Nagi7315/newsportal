package com.gofortrainings.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class UserDetailModelTest {

    AemContext context = new AemContext();

    UserDetailModel userDetailModel;

    @BeforeEach
    void setUp(){
        context.addModelsForClasses(UserDetailModel.class);

        Map<String, Object> props = new HashMap<>();
        props.put("firstName","shashidhar");
        props.put("lastName","reddy");
        props.put("age","27");
        props.put("address","Nagole, Hyd");

        Resource userRes = context.create().resource("/content/user-details", props);
        userDetailModel = userRes.adaptTo(UserDetailModel.class);
    }

    @Test
    void test(){
        assertEquals("shashidhar",userDetailModel.getFirstName());
    }

}