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
- Docker (optional: for containerized deployment)
- Kubernetes cluster (optional: for Kubernetes deployment)
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

#### Option 1: Local Development

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

#### Option 2: Docker

1. Build the Docker image:
```bash
docker build -t spring-ai-langchain:latest .
```

2. Run with Docker:
```bash
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your-openai-api-key \
  -e LANGSMITH_API_KEY=your-langsmith-api-key \
  spring-ai-langchain:latest
```

#### Option 3: Docker Compose (Recommended for Development)

```bash
# Copy environment file and update with your API keys
cp .env.example .env
# Edit .env file with your API keys

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f spring-ai-langchain

# Stop services
docker-compose down
```

This includes:
- Spring AI application
- Ollama (local LLM server)
- Redis (optional caching)
- PostgreSQL (optional database)

#### Option 4: Kubernetes with Helm

1. Build and push Docker image:
```bash
docker build -t your-registry/spring-ai-langchain:1.0.0 .
docker push your-registry/spring-ai-langchain:1.0.0
```

2. Install with Helm:
```bash
# Add your API keys to values
helm install spring-ai-langchain ./helm/spring-ai-langchain \
  --set image.repository=your-registry/spring-ai-langchain \
  --set config.openai.apiKey="your-openai-api-key" \
  --set config.langsmith.apiKey="your-langsmith-api-key"
```

3. For development environment:
```bash
helm install spring-ai-dev ./helm/spring-ai-langchain \
  -f ./helm/spring-ai-langchain/values-dev.yaml \
  --set config.openai.apiKey="your-openai-api-key" \
  --set config.langsmith.apiKey="your-langsmith-api-key"
```

4. For production environment:
```bash
helm install spring-ai-prod ./helm/spring-ai-langchain \
  -f ./helm/spring-ai-langchain/values-prod.yaml \
  --set config.openai.apiKey="your-openai-api-key" \
  --set config.langsmith.apiKey="your-langsmith-api-key"
```

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

## Deployment

### Docker Deployment

#### Multi-stage Dockerfile Features
- **Build Stage**: Uses Eclipse Temurin JDK 17 for compilation
- **Runtime Stage**: Uses Eclipse Temurin JRE 17 for minimal footprint
- **Security**: Runs as non-root user (appuser:1001)
- **Health Checks**: Built-in health monitoring
- **JVM Optimization**: Container-aware memory settings

#### Docker Compose Stack
The `docker-compose.yml` provides a complete development environment:

```yaml
Services:
├── spring-ai-langchain    # Main application
├── ollama                # Local LLM server
├── ollama-init          # Model initialization
├── redis                # Caching (optional)
└── postgres             # Database (optional)
```

### Kubernetes Deployment

#### Helm Chart Features
- **Multi-environment**: Separate values for dev/prod
- **Security**: Pod security contexts and non-root execution
- **Scalability**: HPA with CPU/Memory metrics
- **Monitoring**: Prometheus metrics and health checks
- **Configuration**: ConfigMaps and Secrets management
- **Ingress**: TLS termination and routing

#### Helm Chart Structure
```
helm/spring-ai-langchain/
├── Chart.yaml              # Chart metadata
├── values.yaml             # Default configuration
├── values-dev.yaml         # Development overrides
├── values-prod.yaml        # Production overrides
└── templates/
    ├── deployment.yaml     # Main application deployment
    ├── service.yaml        # Service definition
    ├── ingress.yaml        # Ingress configuration
    ├── configmap.yaml      # Application configuration
    ├── secret.yaml         # API keys and secrets
    ├── serviceaccount.yaml # Service account
    ├── hpa.yaml           # Horizontal Pod Autoscaler
    └── _helpers.tpl       # Template helpers
```

#### Production Features
- **High Availability**: 3+ replicas with pod anti-affinity
- **Auto-scaling**: HPA with CPU and memory targets
- **Security**: Network policies, security contexts, TLS
- **Monitoring**: Prometheus integration, detailed health checks
- **Resource Management**: Optimized CPU/memory requests and limits

### Environment-Specific Configurations

#### Development (`values-dev.yaml`)
- Single replica for resource efficiency
- NodePort service for easy access
- Debug logging enabled
- Cheaper GPT-3.5-turbo model
- Ollama integration enabled

#### Production (`values-prod.yaml`)
- Multiple replicas for high availability
- ClusterIP with Ingress and TLS
- Minimal logging for security
- GPT-4 model for quality
- External secret management
- Pod disruption budgets
- Resource quotas and limits

### Monitoring and Observability

#### Built-in Endpoints
```bash
# Application health
curl http://localhost:8080/api/ai/health

# Spring Actuator endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/info
```

#### Kubernetes Monitoring
```bash
# Check pod status
kubectl get pods -l app.kubernetes.io/name=spring-ai-langchain

# View logs
kubectl logs -f deployment/spring-ai-langchain

# Check HPA status
kubectl get hpa

# Monitor resource usage
kubectl top pods -l app.kubernetes.io/name=spring-ai-langchain
```

#### LangSmith Integration
- Automatic trace generation for all AI operations
- Async trace submission for performance
- Configurable project organization
- Error tracking and debugging support

## Development

### Project Structure
```
spring-ai-langchain/
├── src/main/java/com/example/springai/
│   ├── config/           # Configuration classes
│   ├── controller/       # REST API endpoints
│   ├── dto/             # Data transfer objects
│   ├── langgraph/       # LangGraph-style workflow components
│   ├── langsmith/       # LangSmith tracing integration
│   └── service/         # Business logic services
├── helm/                # Kubernetes Helm charts
│   └── spring-ai-langchain/
│       ├── templates/   # Kubernetes manifests
│       └── values*.yaml # Environment configurations
├── Dockerfile           # Container build instructions
├── docker-compose.yml   # Local development stack
└── .env.example        # Environment variables template
```

### Local Development Setup

1. **Using Docker Compose (Recommended)**:
```bash
cp .env.example .env
# Edit .env with your API keys
docker-compose up -d
```

2. **Using Gradle**:
```bash
./gradlew bootRun
```

### Testing
```bash
# Unit tests
./gradlew test

# Integration tests with Docker
docker-compose -f docker-compose.test.yml up --build --abort-on-container-exit

# Load testing
curl -X POST http://localhost:8080/api/ai/chat/spring-ai \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello World"}'
```

### Building and Deployment

#### Local Build
```bash
./gradlew build
```

#### Docker Build
```bash
# Development build
docker build -t spring-ai-langchain:dev .

# Production build with multi-platform support
docker buildx build --platform linux/amd64,linux/arm64 \
  -t spring-ai-langchain:1.0.0 --push .
```

#### Helm Deployment
```bash
# Lint chart
helm lint ./helm/spring-ai-langchain

# Dry run
helm install --dry-run --debug spring-ai-test ./helm/spring-ai-langchain

# Deploy to development
helm upgrade --install spring-ai-dev ./helm/spring-ai-langchain \
  -f ./helm/spring-ai-langchain/values-dev.yaml

# Deploy to production
helm upgrade --install spring-ai-prod ./helm/spring-ai-langchain \
  -f ./helm/spring-ai-langchain/values-prod.yaml
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