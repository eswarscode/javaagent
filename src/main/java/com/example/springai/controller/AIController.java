package com.example.springai.controller;

import com.example.springai.dto.ChatRequest;
import com.example.springai.dto.ChatResponse;
import com.example.springai.dto.WorkflowRequest;
import com.example.springai.dto.WorkflowResponse;
import com.example.springai.langgraph.Workflow;
import com.example.springai.langgraph.WorkflowService;
import com.example.springai.langgraph.WorkflowState;
import com.example.springai.langsmith.LangSmithTracer;
import com.example.springai.service.LangChainService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    private final ChatClient springAiChatClient;
    private final LangChainService langChainService;
    private final WorkflowService workflowService;
    private final LangSmithTracer langSmithTracer;

    @Autowired
    public AIController(ChatClient springAiChatClient,
                       LangChainService langChainService,
                       WorkflowService workflowService,
                       LangSmithTracer langSmithTracer) {
        this.springAiChatClient = springAiChatClient;
        this.langChainService = langChainService;
        this.workflowService = workflowService;
        this.langSmithTracer = langSmithTracer;
    }

    @PostMapping("/chat/spring-ai")
    public ResponseEntity<com.example.springai.dto.ChatResponse> chatWithSpringAi(
            @Valid @RequestBody ChatRequest request) {
        
        return langSmithTracer.traceExecution("spring-ai-chat",
                Map.of("prompt", request.getMessage()),
                () -> {
                    ChatResponse response = springAiChatClient.call(new Prompt(request.getMessage()));
                    String content = response.getResult().getOutput().getContent();
                    
                    return ResponseEntity.ok(new com.example.springai.dto.ChatResponse(content));
                });
    }

    @PostMapping("/chat/langchain")
    public ResponseEntity<com.example.springai.dto.ChatResponse> chatWithLangChain(
            @Valid @RequestBody ChatRequest request) {
        
        return langSmithTracer.traceExecution("langchain-chat",
                Map.of("prompt", request.getMessage()),
                () -> {
                    String response = langChainService.generateResponse(request.getMessage());
                    return ResponseEntity.ok(new com.example.springai.dto.ChatResponse(response));
                });
    }

    @PostMapping("/workflow")
    public ResponseEntity<WorkflowResponse> executeWorkflow(
            @Valid @RequestBody WorkflowRequest request) {
        
        return langSmithTracer.traceExecution("langgraph-workflow",
                Map.of("input", request.getInput()),
                () -> {
                    Workflow workflow = workflowService.createSampleWorkflow();
                    WorkflowState initialState = new WorkflowState();
                    initialState.put("input", request.getInput());
                    
                    WorkflowState result = workflow.execute(initialState);
                    
                    WorkflowResponse response = new WorkflowResponse();
                    response.setResponse(result.get("response"));
                    response.setProcessedInput(result.get("processed_input"));
                    response.setWordCount(result.get("word_count"));
                    response.setIsQuestion(result.get("is_question"));
                    response.setIsRequest(result.get("is_request"));
                    response.setSentiment(result.get("sentiment"));
                    response.setGenerationCount(result.get("generation_count"));
                    
                    return ResponseEntity.ok(response);
                });
    }

    @PostMapping("/embedding")
    public ResponseEntity<Map<String, Object>> generateEmbedding(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Text is required"));
        }
        
        return langSmithTracer.traceExecution("generate-embedding",
                Map.of("text", text),
                () -> {
                    float[] embedding = langChainService.generateEmbedding(text);
                    return ResponseEntity.ok(Map.of(
                        "embedding", embedding,
                        "dimension", embedding.length,
                        "text", text
                    ));
                });
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "spring-ai", "available",
            "langchain", "available",
            "langgraph", "available",
            "langsmith", "available"
        ));
    }
}