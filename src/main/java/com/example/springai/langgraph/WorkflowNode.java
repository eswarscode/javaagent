package com.example.springai.langgraph;

@FunctionalInterface
public interface WorkflowNode {
    WorkflowState execute(WorkflowState state);
}