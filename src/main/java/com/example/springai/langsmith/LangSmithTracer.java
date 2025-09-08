package com.example.springai.langsmith;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class LangSmithTracer {
    
    private static final Logger logger = LoggerFactory.getLogger(LangSmithTracer.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final OkHttpClient httpClient;
    private final LangSmithConfig config;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public LangSmithTracer(OkHttpClient langSmithHttpClient, 
                          LangSmithConfig config) {
        this.httpClient = langSmithHttpClient;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }
    
    public TraceData startTrace(String name, Map<String, Object> inputs) {
        TraceData trace = new TraceData();
        trace.setProjectName(config.getProjectName());
        trace.setName(name);
        trace.setInputs(inputs);
        trace.setStartTime(Instant.now());
        trace.setTags(new String[]{"spring-ai", "java"});
        
        logger.debug("Started trace: {} with ID: {}", name, trace.getId());
        return trace;
    }
    
    public void endTrace(TraceData trace, Map<String, Object> outputs) {
        trace.setEndTime(Instant.now());
        trace.setOutputs(outputs);
        
        CompletableFuture.runAsync(() -> sendTraceAsync(trace));
    }
    
    public void endTraceWithError(TraceData trace, String error) {
        trace.setEndTime(Instant.now());
        trace.setError(error);
        
        CompletableFuture.runAsync(() -> sendTraceAsync(trace));
    }
    
    private void sendTraceAsync(TraceData trace) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(trace);
            
            RequestBody body = RequestBody.create(jsonPayload, JSON);
            Request request = new Request.Builder()
                    .url(config.getEndpoint() + "/runs")
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    logger.warn("Failed to send trace to LangSmith: {} - {}", 
                              response.code(), response.message());
                } else {
                    logger.debug("Successfully sent trace: {}", trace.getId());
                }
            }
        } catch (IOException e) {
            logger.error("Error sending trace to LangSmith", e);
        }
    }
    
    public <T> T traceExecution(String operationName, 
                               Map<String, Object> inputs,
                               TracedOperation<T> operation) {
        TraceData trace = startTrace(operationName, inputs);
        
        try {
            T result = operation.execute();
            
            Map<String, Object> outputs = Map.of("result", result);
            endTrace(trace, outputs);
            
            return result;
        } catch (Exception e) {
            endTraceWithError(trace, e.getMessage());
            throw new RuntimeException("Traced operation failed", e);
        }
    }
    
    @FunctionalInterface
    public interface TracedOperation<T> {
        T execute() throws Exception;
    }
}