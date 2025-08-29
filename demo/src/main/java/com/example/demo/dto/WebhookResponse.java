package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookResponse {

    // This annotation ensures it correctly maps the JSON key "webhookUrl"
    // to this Java field, even if the naming is slightly different.
    @JsonProperty("webhook")
    private String webhookUrl; // Changed from webhookURL to webhookUrl (camelCase)

    private String accessToken;

    // Getters and Setters
    public String getWebhookURL() {
        return webhookUrl;
    }

    public void setWebhookURL(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}