package com.example.springai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ChatResponse {
    
    @JsonProperty("response")
    private String response;
    
    @JsonProperty("timestamp")
    private Instant timestamp;

    public ChatResponse() {
        this.timestamp = Instant.now();
    }

    public ChatResponse(String response) {
        this.response = response;
        this.timestamp = Instant.now();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}