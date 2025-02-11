package com.jumpcloud.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumpcloud.connector.JumpCloudConfiguration;
import com.jumpcloud.util.JumpCloudFilter;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import org.identityconnectors.framework.spi.SearchResultsHandler;

public class JumpCloudApiImpl implements JumpCloudApi {

    private static final Logger LOG = LoggerFactory.getLogger(JumpCloudApiImpl.class);
    private final JumpCloudConfiguration configuration;

    public JumpCloudApiImpl(JumpCloudConfiguration configuration) {
        this.configuration = configuration;
    }

     @Override
    public void searchUsers(JumpCloudFilter filter, SearchResultsHandler resultsHandler, OperationOptions options) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(configuration.getApiURL() + "/users");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("x-api-key", configuration.getApiKey());

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode >= 200 && statusCode < 300) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode usersArray = objectMapper.readTree(response.getEntity().getContent());

                for (JsonNode userNode : usersArray) {
                    ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
                    builder.setObjectClass(ObjectClass.ACCOUNT);

                    String userId = userNode.get("id").asText();
                    String userName = userNode.get("username").asText();
                    String email = userNode.get("email").asText();
                    String firstName = userNode.get("firstname").asText();
                    String lastName = userNode.get("lastname").asText();

                    builder.addAttribute(AttributeBuilder.build(Uid.NAME, userId));
                    builder.addAttribute(AttributeBuilder.build(Name.NAME, userName));
                    builder.addAttribute(AttributeBuilder.build("email", email));
                    builder.addAttribute(AttributeBuilder.build("firstname", firstName));
                    builder.addAttribute(AttributeBuilder.build("lastname", lastName));

                    ConnectorObject connectorObject = builder.build();
                    resultsHandler.handle(connectorObject);
                }

            } else {
                LOG.error("Failed to search users. Status code: {}, Response: {}", statusCode, response);
                throw new ConnectorException("Failed to search users. Status code: " + statusCode);
            }

        } catch (IOException e) {
            LOG.error("Error searching users: {}", e.getMessage(), e);
            throw new ConnectorException("Error searching users: " + e.getMessage(), e);
        }
    }
}