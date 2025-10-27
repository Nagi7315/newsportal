package com.gofortrainings.newsportal.core.services.impl;

import com.gofortrainings.newsportal.core.services.CountryDetailsService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(service = CountryDetailsService.class, immediate = true)
@Designate(ocd = CountryDetailsServiceImpl.Configuration.class)
public class CountryDetailsServiceImpl implements CountryDetailsService {

    private String countryApiUrl;

    private static final Logger log = LoggerFactory.getLogger(CountryDetailsServiceImpl.class);

    @ObjectClassDefinition
    public @interface Configuration {
        @AttributeDefinition(name = "Country Rest API")
        public String countryRestApi() default "https://api.first.org/data/v1/countries";

    }

    @Activate
    @Modified
    public void update(CountryDetailsServiceImpl.Configuration config) {
        this.countryApiUrl = config.countryRestApi();
        log.info("Country Info {}", fetchCountryDetails());
    }

    @Override
    public String fetchCountryDetails() {
        String countryData;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(countryApiUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                countryData = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return countryData;
    }
}
