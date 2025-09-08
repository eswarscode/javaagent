package com.example.springai.langgraph;

import java.util.HashMap;
import java.util.Map;

public class WorkflowState {
    private final Map<String, Object> data;
    
    public WorkflowState() {
        this.data = new HashMap<>();
    }
    
    public WorkflowState(Map<String, Object> initialData) {
        this.data = new HashMap<>(initialData);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) data.get(key);
    }
    
    public <T> T get(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }
    
    public void put(String key, Object value) {
        data.put(key, value);
    }
    
    public void putAll(Map<String, Object> values) {
        data.putAll(values);
    }
    
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }
    
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }
    
    public WorkflowState copy() {
        return new WorkflowState(this.data);
    }
    
    @Override
    public String toString() {
        return "WorkflowState{data=" + data + "}";
    }
}