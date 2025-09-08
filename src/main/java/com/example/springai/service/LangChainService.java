package com.example.springai.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LangChainService {

    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final ChatMemory chatMemory;
    private final AssistantAi assistant;

    @Autowired
    public LangChainService(ChatLanguageModel chatModel, 
                           EmbeddingModel embeddingModel, 
                           ChatMemory chatMemory) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.chatMemory = chatMemory;
        this.assistant = AiServices.builder(AssistantAi.class)
                .chatLanguageModel(chatModel)
                .chatMemory(chatMemory)
                .build();
    }

    public String generateResponse(String prompt) {
        return assistant.chat(prompt);
    }

    public String generateSimpleResponse(String prompt) {
        return chatModel.generate(prompt);
    }

    public float[] generateEmbedding(String text) {
        return embeddingModel.embed(text).content().vector();
    }

    interface AssistantAi {
        String chat(String message);
    }
}