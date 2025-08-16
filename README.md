# Spring Boot 3 Kotlin Hello World

A simple Spring Boot 3 application written in Kotlin with REST API endpoints, monitoring capabilities, and AWS S3 multipart upload functionality.

## Prerequisites

- Java 17 or higher
- Gradle (wrapper included)
- AWS credentials (for S3 functionality)

## Getting Started

### 1. AWS Credentials Setup (Optional)

For S3 functionality, copy the example environment file and configure your AWS credentials:

```bash
cp .env.example .env
```

Edit `.env` file with your actual AWS credentials:

```bash
# AWS Credentials Configuration
AWS_ACCESS_KEY_ID=your_access_key_id_here
AWS_SECRET_ACCESS_KEY=your_secret_access_key_here
AWS_REGION=ap-northeast-1
AWS_S3_BUCKET_NAME=your-test-bucket-name
```

**Note**: The `.env` file is gitignored to prevent credential exposure. If no credentials are provided, the application will fall back to the AWS default credential chain.

### 2. Build the application

```bash
./gradlew build
```

### 3. Run the application

```bash
./gradlew bootRun
```

The application will start on port 8090 by default.

### Test the application

#### Hello World Endpoint
```bash
curl http://localhost:8090/api/v1/hello
```

#### S3 Upload Endpoints
```bash
# Upload file to S3
curl -X POST http://localhost:8090/api/s3/upload \
  -F "file=@/path/to/your/file.txt" \
  -F "bucketName=your-bucket-name" \
  -F "key=optional-object-key"

# Upload file with progress tracking
curl -X POST http://localhost:8090/api/s3/upload-with-progress \
  -F "file=@/path/to/your/file.txt" \
  -F "bucketName=your-bucket-name"
```

Expected response:
```json
{"message": "Hello World!"}
```

#### Health Check Endpoint
```bash
curl http://localhost:8080/api/v1/health
```

Expected response:
```json
{"status": "UP", "service": "hello-service"}
```

#### Spring Boot Actuator Endpoints
```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Info endpoint
curl http://localhost:8080/actuator/info

# Metrics endpoint
curl http://localhost:8080/actuator/metrics
```

### Run Tests

```bash
./gradlew test
```

## API Documentation

### Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/v1/hello` | Returns hello message |
| GET | `/api/v1/health` | Returns service health status |
| GET | `/actuator/health` | Spring Boot health check |
| GET | `/actuator/info` | Application information |
| GET | `/actuator/metrics` | Application metrics |

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/example/demo/
│   │       ├── DemoApplication.kt      # Main application class
│   │       └── HelloController.kt      # REST controller
│   └── resources/
│       └── application.yml             # Configuration
└── test/
    └── kotlin/
        └── com/example/demo/
            └── HelloControllerTest.kt   # Unit tests
```

## Technologies Used

- Spring Boot 3.2.5
- Spring Boot Actuator (monitoring)
- Kotlin 1.9.24
- Gradle 8.14.3
- Java 17+
- JUnit 5 (testing)

## Configuration

The application can be configured via `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: spring-boot-kotlin-hello-world

logging:
  level:
    com.example.demo: INFO

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```