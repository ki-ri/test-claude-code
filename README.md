# Spring Boot 3 Kotlin Hello World

A simple Spring Boot 3 application written in Kotlin that returns "Hello World!" message.

## Prerequisites

- Java 17 or higher
- Gradle (wrapper included)

## Getting Started

### Build the application

```bash
./gradlew build
```

### Run the application

```bash
./gradlew bootRun
```

The application will start on port 8080 by default. To use a different port:

```bash
./gradlew bootRun --args='--server.port=9090'
```

### Test the application

Open your browser or use curl to access:

```bash
curl http://localhost:8080/
```

Expected response: `Hello World!`

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/example/demo/
│   │       ├── DemoApplication.kt      # Main application class
│   │       └── HelloController.kt      # REST controller
│   └── resources/
└── test/
```

## Technologies Used

- Spring Boot 3.2.0
- Kotlin 1.9.20
- Gradle 8.14.3
- Java 17+