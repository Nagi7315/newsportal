package com.gofortrainings.newsportal.core.services.impl;

import com.day.cq.replication.ReplicationActionType;
import com.gofortrainings.newsportal.core.configuration.ElasticSearchConfiguration;
import com.gofortrainings.newsportal.core.services.ElasticsearchService;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(service = ElasticsearchService.class, immediate = true)
@Designate(ocd = ElasticSearchConfiguration.class)
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    private String serverApiUsername;

    private String serverApiPassword;

    private String elasticSearchEndpointUrl;

    protected CloseableHttpClient httpClient;


    @Activate
    @Modified
    public void activate(ElasticSearchConfiguration config) {
        this.serverApiUsername = config.serverApiUsername();
        this.elasticSearchEndpointUrl = config.elasticSearchEndpointUrl();
        this.serverApiPassword = config.serverApiPassword();

        initialize();
    }

    public void initialize() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

        // Increase max total connection
        connManager.setMaxTotal(20);
        // Increase default max connection per route
        connManager.setDefaultMaxPerRoute(5);

        // config timeout
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10 * 1000)
                .setConnectionRequestTimeout(10 * 1000)
                .setSocketTimeout(10 * 1000).build();

        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(config).build();
    }

    @Override
    public int sendEvent(String jsonBody, String topic, Resource resource, String actionType) throws IOException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.serverApiUsername, this.serverApiPassword));
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCredentialsProvider(credentialsProvider);

        String uniqueId = String.valueOf(Math.abs(resource.getPath().hashCode()));

        String apiUrl = elasticSearchEndpointUrl + "/practice-index" + "/_doc/" + uniqueId;

        if (ReplicationActionType.DEACTIVATE.getName().equalsIgnoreCase(topic)) {
            return delete(httpClient, apiUrl, localContext);
        } else {
            return execute(httpClient, apiUrl, jsonBody, localContext);
        }
    }

    private int delete(CloseableHttpClient httpClient, String apiPath,
                       HttpClientContext localContext) {
        LOG.info("Entry in the execute(): apiPath {}", apiPath);
        HttpDelete deleteMethod = new HttpDelete(apiPath);
        int statusCode = 0;

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(deleteMethod, localContext);
            statusCode = response.getStatusLine().getStatusCode();
            LOG.info("Layer return the {} status code", statusCode);

        } catch (IOException e) {
            LOG.error("IOException", e.getMessage());
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("IOException", e.getMessage());
                }
            }
        }
        return statusCode;
    }

    /*
     * This represents only the most basic contract for HTTP request execution.
     * Executes HTTP client using the default context and returns the response
     * to the request
     */
    private int execute(CloseableHttpClient httpClient, String apiPath, String json,
                        HttpClientContext localContext) {
        LOG.info("Entry in the execute(): apiPath {}", apiPath);

        StringEntity requestEntity = new StringEntity(json, "UTF-8");
        HttpPut putMethod = new HttpPut(apiPath);
        putMethod.addHeader("content-type", "application/json");
        putMethod.setEntity(requestEntity);
        int statusCode = 0;

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(putMethod, localContext);
            statusCode = response.getStatusLine().getStatusCode();
            LOG.info("Layer return the {} status code", statusCode);

        } catch (IOException e) {
            LOG.error("IOException", e.getMessage());
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("IOException", e.getMessage());
                }
            }
        }
        return statusCode;
    }
}
