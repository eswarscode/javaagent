package com.example.springai.langsmith;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class TraceData {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("project_name")
    private String projectName;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("start_time")
    private Instant startTime;
    
    @JsonProperty("end_time")
    private Instant endTime;
    
    @JsonProperty("inputs")
    private Map<String, Object> inputs;
    
    @JsonProperty("outputs")
    private Map<String, Object> outputs;
    
    @JsonProperty("run_type")
    private String runType;
    
    @JsonProperty("execution_order")
    private Integer executionOrder;
    
    @JsonProperty("extra")
    private Map<String, Object> extra;
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("tags")
    private String[] tags;

    public TraceData() {
        this.id = UUID.randomUUID().toString();
        this.startTime = Instant.now();
        this.runType = "chain";
        this.executionOrder = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Object> inputs) {
        this.inputs = inputs;
    }

    public Map<String, Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, Object> outputs) {
        this.outputs = outputs;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}