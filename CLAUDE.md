# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Author: ki-ri

## Project Overview

This is a Spring Boot 3 Kotlin Hello World application that demonstrates modern Java development practices with automated testing and CI/CD pipeline.

### Key Features
- REST API with versioned endpoints
- Spring Boot Actuator for monitoring
- Comprehensive unit testing
- GitHub Actions CI/CD pipeline
- Production-ready configuration

## Development Commands

### Build and Test
```bash
# Build the application
./gradlew build

# Run tests
./gradlew test

# Run the application
./gradlew bootRun

# Generate JAR file
./gradlew bootJar
```

### API Testing
```bash
# Hello World endpoint
curl http://localhost:8080/api/v1/hello

# Health check endpoint
curl http://localhost:8080/api/v1/health

# Actuator endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
curl http://localhost:8080/actuator/metrics
```

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.2.5
- **Language**: Kotlin 1.9.24
- **Build Tool**: Gradle 8.14.3
- **Testing**: JUnit 5, MockMvc
- **Monitoring**: Spring Boot Actuator
- **CI/CD**: GitHub Actions

### Project Structure
```
src/
├── main/
│   ├── kotlin/com/example/demo/
│   │   ├── DemoApplication.kt      # Main application class
│   │   └── HelloController.kt      # REST controller
│   └── resources/
│       └── application.yml         # Configuration
└── test/kotlin/com/example/demo/
    └── HelloControllerTest.kt       # Unit tests
```

### API Design
- **Base Path**: `/api/v1`
- **Content Type**: `application/json`
- **HTTP Status Codes**: Standard REST conventions
- **Error Handling**: Spring Boot default error handling

## Coding Guidelines

- Always use descriptive variable names (常に説明的な変数名を使用する)
- Follow Kotlin coding conventions
- Write comprehensive unit tests for all endpoints
- Use dependency injection for better testability
- Configure proper logging levels for different environments