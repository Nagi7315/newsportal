package com.gofortrainings.newsportal.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "ElasticSearch OCD Configuration",
        description = "ElasticSearch OCD Configuration description")
public @interface ElasticSearchConfiguration {

    @AttributeDefinition(
            name = "Elasticsearch API username",
            description = "Elasticsearch API username",
            type = AttributeType.STRING)
    String serverApiUsername();

    @AttributeDefinition(
            name = "Elasticsearch API password",
            description = "Elasticsearch API password",
            type = AttributeType.STRING)
    String serverApiPassword();

    @AttributeDefinition(
            name = "Elasticsearch API Endpoint URL",
            description = "Elasticsearch API Endpoint URL",
            type = AttributeType.STRING)
    String elasticSearchEndpointUrl();
}
