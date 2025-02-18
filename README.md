# Customer Microservice

Spring Boot Webflux microservice that handles customer operations.

## Stack
- Java 11
- Spring Boot 2.x
- Spring Webflux
- Spring Cloud Config Client
- Reactive Mongodb
- Openapi contract first
- Swagger ui

## Configuration
Service connects to Config Server using:
```properties
spring.application.name=ms-customer-service
spring.config.import=optional:configserver:http://localhost:8888
```
for properties
```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ms-customer-service

server:
  port: 8090
```

## Swagger
http://localhost:8090/swagger-ui.html

![ms-customer-service-v2-2025-02-11-193301](https://github.com/user-attachments/assets/203bbe02-e5dc-44fe-8ebe-fa8e3aea9444)

