# Spring AI with LangChain, LangGraph, and LangSmith

A comprehensive Spring Boot application that integrates Spring AI, LangChain4j, custom LangGraph-style workflows, and LangSmith tracing for advanced AI-powered applications.

## Features

- **Spring AI Integration**: Native Spring Boot AI capabilities with OpenAI and Ollama support
- **LangChain4j Integration**: Advanced language model chaining and conversation memory
- **LangGraph-style Workflows**: Custom Java implementation of workflow patterns inspired by LangGraph
- **LangSmith Tracing**: Comprehensive monitoring and tracing of AI operations
- **REST API**: Clean RESTful endpoints for all AI operations
- **Async Processing**: Non-blocking trace submissions to LangSmith

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           REST API Layer                                │
├─────────────────────────────────────────────────────────────────────────┤
│  Spring AI     │  LangChain4j    │  LangGraph      │  LangSmith        │
│  Integration   │  Service        │  Workflows      │  Tracing          │
├─────────────────────────────────────────────────────────────────────────┤
│                         Business Logic Layer                            │
├─────────────────────────────────────────────────────────────────────────┤
│                    Configuration & Infrastructure                        │
└─────────────────────────────────────────────────────────────────────────┘
```

## Quick Start

### Prerequisites

- Java 17+
- Gradle 8.5+
- OpenAI API Key (optional: for AI features)
- LangSmith API Key (optional: for tracing)

### Environment Variables

Create a `.env` file or set the following environment variables:

```bash
OPENAI_API_KEY=your-openai-api-key-here
LANGSMITH_API_KEY=your-langsmith-api-key-here
LANGSMITH_PROJECT=spring-ai-demo
```

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd spring-ai-langchain
```

2. Build the application:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check
```http
GET /api/ai/health
```

### Spring AI Chat
```http
POST /api/ai/chat/spring-ai
Content-Type: application/json

{
  "message": "Hello, how can you help me today?"
}
```

### LangChain Chat
```http
POST /api/ai/chat/langchain
Content-Type: application/json

{
  "message": "Explain quantum computing in simple terms"
}
```

### LangGraph Workflow
```http
POST /api/ai/workflow
Content-Type: application/json

{
  "input": "Can you help me understand machine learning?"
}
```

### Generate Embeddings
```http
POST /api/ai/embedding
Content-Type: application/json

{
  "text": "This is a sample text for embedding generation"
}
```

## Configuration

### Application Properties

The application uses `application.yml` for configuration:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:your-openai-api-key}
      chat:
        options:
          model: gpt-4
          temperature: 0.7

langchain4j:
  open-ai:
    api-key: ${OPENAI_API_KEY:your-openai-api-key}
    chat-model:
      model-name: gpt-4
      temperature: 0.7

langsmith:
  api-key: ${LANGSMITH_API_KEY:your-langsmith-api-key}
  project-name: ${LANGSMITH_PROJECT:spring-ai-demo}
  endpoint: ${LANGSMITH_ENDPOINT:https://api.smith.langchain.com}
```

## Components Overview

### Spring AI Integration (`/config`, `/controller`)
- Native Spring Boot AI capabilities
- OpenAI and Ollama model support
- Automatic configuration and beans

### LangChain4j Service (`/service`)
- Advanced conversation management
- Memory-enabled chat sessions
- Embedding generation capabilities

### LangGraph Workflows (`/langgraph`)
- **WorkflowState**: Manages state throughout workflow execution
- **WorkflowNode**: Functional interface for workflow steps
- **Workflow**: Orchestrates node execution with conditional edges
- **WorkflowService**: Pre-built workflows for common patterns

### LangSmith Tracing (`/langsmith`)
- **TraceData**: Structured trace information
- **LangSmithTracer**: Async trace submission to LangSmith
- **LangSmithConfig**: Configuration and HTTP client setup

## Workflow Examples

The sample workflow demonstrates a multi-step AI processing pipeline:

1. **Input Processing**: Cleans and analyzes input text
2. **Content Analysis**: Determines if input is a question, request, and sentiment
3. **Response Generation**: Creates AI responses using LangChain
4. **Quality Review**: Evaluates response quality and triggers regeneration if needed

```java
Workflow workflow = new Workflow()
    .addNode("input", this::processInput)
    .addNode("analyze", this::analyzeContent)
    .addNode("generate", this::generateResponse)
    .addNode("review", this::reviewResponse)
    .addEdge("input", "analyze")
    .addEdge("analyze", "generate")
    .addConditionalEdge("generate", this::shouldReview)
    .setEntryPoint("input");
```

## Monitoring and Observability

### LangSmith Integration
- Automatic trace generation for all AI operations
- Async trace submission for performance
- Configurable project organization
- Error tracking and debugging support

### Spring Actuator
- Health checks and metrics at `/actuator/*`
- Application status monitoring
- Performance metrics

## Development

### Project Structure
```
src/main/java/com/example/springai/
├── config/           # Configuration classes
├── controller/       # REST API endpoints
├── dto/             # Data transfer objects
├── langgraph/       # LangGraph-style workflow components
├── langsmith/       # LangSmith tracing integration
└── service/         # Business logic services
```

### Testing
```bash
./gradlew test
```

### Building
```bash
./gradlew build
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions and support:
- Create an issue in the GitHub repository
- Check the documentation in the `/docs` folder
- Review the API examples in the `/examples` folder