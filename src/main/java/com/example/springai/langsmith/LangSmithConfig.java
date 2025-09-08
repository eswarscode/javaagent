package com.example.springai.langsmith;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LangSmithConfig {

    @Value("${langsmith.api-key}")
    private String apiKey;

    @Value("${langsmith.endpoint:https://api.smith.langchain.com}")
    private String endpoint;

    @Value("${langsmith.project-name:spring-ai-demo}")
    private String projectName;

    @Bean
    public OkHttpClient langSmithHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(30))
                .addInterceptor(chain -> {
                    var request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + apiKey)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getProjectName() {
        return projectName;
    }
}