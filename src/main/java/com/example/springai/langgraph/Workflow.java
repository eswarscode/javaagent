package com.example.springai.langgraph;

import java.util.*;
import java.util.function.Function;

public class Workflow {
    private final Map<String, WorkflowNode> nodes;
    private final Map<String, Function<WorkflowState, String>> edges;
    private String startNode;
    private final Set<String> endNodes;
    
    public Workflow() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.endNodes = new HashSet<>();
    }
    
    public Workflow addNode(String name, WorkflowNode node) {
        nodes.put(name, node);
        return this;
    }
    
    public Workflow addEdge(String from, String to) {
        edges.put(from, state -> to);
        return this;
    }
    
    public Workflow addConditionalEdge(String from, Function<WorkflowState, String> condition) {
        edges.put(from, condition);
        return this;
    }
    
    public Workflow setEntryPoint(String nodeName) {
        this.startNode = nodeName;
        return this;
    }
    
    public Workflow addEndNode(String nodeName) {
        this.endNodes.add(nodeName);
        return this;
    }
    
    public WorkflowState execute(WorkflowState initialState) {
        if (startNode == null) {
            throw new IllegalStateException("Start node not set");
        }
        
        WorkflowState currentState = initialState.copy();
        String currentNode = startNode;
        
        while (currentNode != null && !endNodes.contains(currentNode)) {
            WorkflowNode node = nodes.get(currentNode);
            if (node == null) {
                throw new IllegalStateException("Node not found: " + currentNode);
            }
            
            currentState = node.execute(currentState);
            
            Function<WorkflowState, String> edge = edges.get(currentNode);
            if (edge != null) {
                currentNode = edge.apply(currentState);
            } else {
                currentNode = null;
            }
        }
        
        return currentState;
    }
    
    public Set<String> getNodeNames() {
        return new HashSet<>(nodes.keySet());
    }
    
    public boolean hasNode(String nodeName) {
        return nodes.containsKey(nodeName);
    }
}