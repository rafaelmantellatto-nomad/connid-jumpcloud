package com.jumpcloud.connector;

import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class JumpCloudConfiguration extends AbstractConfiguration {

    private String apiKey;
    private String apiURL = "https://console.jumpcloud.com/api/v2"; // URL Base da API

    @ConfigurationProperty(displayMessageKey = "apiKey.display",
            helpMessageKey = "apiKey.help", required = true, order = 1)
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @ConfigurationProperty(displayMessageKey = "apiURL.display",
            helpMessageKey = "apiURL.help", required = false, order = 2)
    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    @Override
    public void validate() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key cannot be null or empty.");
        }
        // Adicione outras validações conforme necessário.
    }
}