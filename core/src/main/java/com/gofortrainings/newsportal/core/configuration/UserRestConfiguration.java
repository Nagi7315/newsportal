package com.gofortrainings.newsportal.core.configuration;

import com.gofortrainings.newsportal.core.utils.Constants;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface UserRestConfiguration {

    @AttributeDefinition(name = "Users Rest API")
    public String userRest_API() default Constants.USERS_REST_API;

    @AttributeDefinition(name = "Users Update Rest API")
    public String usersUpdateRest_API() default Constants.USERS_UPDATE_REST_API;
}
