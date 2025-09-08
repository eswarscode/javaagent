package com.example.springai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WorkflowRequest {
    
    @NotBlank(message = "Input cannot be blank")
    @Size(max = 5000, message = "Input must be less than 5,000 characters")
    private String input;

    public WorkflowRequest() {}

    public WorkflowRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}