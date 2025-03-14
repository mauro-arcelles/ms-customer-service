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
eureka:
  instance:
    hostname: localhost
    instance-id: ${spring.application.name}:${random.int}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ms-bootcamp-arcelles
  redis:
    host: localhost
    port: 6379

server:
  port: ${PORT:0}

customer:
  config:
    personal:
      vip:
        avgDailyMinimumAmount: 100

springdoc:
  api-docs:
    path: /customer-docs/v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
  webjars:
    prefix:
```

## Swagger
http://localhost:8090/swagger-ui.html

![ms-customer-service-v2-2025-02-11-193301](https://github.com/user-attachments/assets/203bbe02-e5dc-44fe-8ebe-fa8e3aea9444)

