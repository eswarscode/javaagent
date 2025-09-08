package com.example.springai.langgraph;

import com.example.springai.service.LangChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
    
    private final LangChainService langChainService;
    
    @Autowired
    public WorkflowService(LangChainService langChainService) {
        this.langChainService = langChainService;
    }
    
    public Workflow createSampleWorkflow() {
        return new Workflow()
            .addNode("input", this::processInput)
            .addNode("analyze", this::analyzeContent)
            .addNode("generate", this::generateResponse)
            .addNode("review", this::reviewResponse)
            .addEdge("input", "analyze")
            .addEdge("analyze", "generate")
            .addConditionalEdge("generate", this::shouldReview)
            .addEdge("review", "generate")
            .setEntryPoint("input")
            .addEndNode("generate");
    }
    
    private WorkflowState processInput(WorkflowState state) {
        String input = state.get("input", "");
        state.put("processed_input", input.trim().toLowerCase());
        state.put("word_count", input.split("\\s+").length);
        return state;
    }
    
    private WorkflowState analyzeContent(WorkflowState state) {
        String input = state.get("processed_input", "");
        
        boolean isQuestion = input.contains("?");
        boolean isRequest = input.contains("please") || input.contains("can you");
        
        state.put("is_question", isQuestion);
        state.put("is_request", isRequest);
        state.put("sentiment", analyzeSentiment(input));
        
        return state;
    }
    
    private WorkflowState generateResponse(WorkflowState state) {
        String input = state.get("input", "");
        boolean needsReview = state.get("needs_review", false);
        
        String prompt = buildPrompt(state, needsReview);
        String response = langChainService.generateResponse(prompt);
        
        state.put("response", response);
        state.put("generation_count", state.get("generation_count", 0) + 1);
        
        return state;
    }
    
    private WorkflowState reviewResponse(WorkflowState state) {
        String response = state.get("response", "");
        
        boolean isAppropriate = response.length() > 10 && !response.contains("error");
        state.put("review_passed", isAppropriate);
        state.put("needs_review", !isAppropriate);
        
        if (!isAppropriate) {
            state.put("review_feedback", "Response needs improvement");
        }
        
        return state;
    }
    
    private String shouldReview(WorkflowState state) {
        int generationCount = state.get("generation_count", 0);
        boolean needsReview = state.get("needs_review", false);
        
        if (generationCount == 1 && !needsReview) {
            return "review";
        } else if (needsReview && generationCount < 3) {
            return "review";
        }
        
        return null;
    }
    
    private String buildPrompt(WorkflowState state, boolean needsReview) {
        String input = state.get("input", "");
        boolean isQuestion = state.get("is_question", false);
        boolean isRequest = state.get("is_request", false);
        
        StringBuilder prompt = new StringBuilder();
        
        if (needsReview) {
            String feedback = state.get("review_feedback", "");
            prompt.append("Please improve your previous response based on this feedback: ")
                  .append(feedback).append(". ");
        }
        
        prompt.append("User input: ").append(input);
        
        if (isQuestion) {
            prompt.append(" (This is a question, please provide a direct answer)");
        } else if (isRequest) {
            prompt.append(" (This is a request, please be helpful and specific)");
        }
        
        return prompt.toString();
    }
    
    private String analyzeSentiment(String text) {
        if (text.contains("great") || text.contains("awesome") || text.contains("good")) {
            return "positive";
        } else if (text.contains("bad") || text.contains("terrible") || text.contains("awful")) {
            return "negative";
        }
        return "neutral";
    }
}