package com.example.springai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class WorkflowResponse {
    
    @JsonProperty("response")
    private String response;
    
    @JsonProperty("processed_input")
    private String processedInput;
    
    @JsonProperty("word_count")
    private Integer wordCount;
    
    @JsonProperty("is_question")
    private Boolean isQuestion;
    
    @JsonProperty("is_request")
    private Boolean isRequest;
    
    @JsonProperty("sentiment")
    private String sentiment;
    
    @JsonProperty("generation_count")
    private Integer generationCount;
    
    @JsonProperty("timestamp")
    private Instant timestamp;

    public WorkflowResponse() {
        this.timestamp = Instant.now();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getProcessedInput() {
        return processedInput;
    }

    public void setProcessedInput(String processedInput) {
        this.processedInput = processedInput;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Boolean getIsQuestion() {
        return isQuestion;
    }

    public void setIsQuestion(Boolean isQuestion) {
        this.isQuestion = isQuestion;
    }

    public Boolean getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(Boolean isRequest) {
        this.isRequest = isRequest;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Integer getGenerationCount() {
        return generationCount;
    }

    public void setGenerationCount(Integer generationCount) {
        this.generationCount = generationCount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}