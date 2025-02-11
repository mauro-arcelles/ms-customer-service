# Customer Microservice

Spring Boot Webflux microservice that handles customer operations.

## Stack
- Java 11
- Spring Boot 2.x
- Spring Webflux
- Spring Cloud Config Client
- Reactive Mongodb
- Openapi contract first

## Configuration
Service connects to Config Server for properties:
```properties
spring.application.name=ms-customer-service
spring.config.import=optional:configserver:http://localhost:8888

![ms-customer-service](https://github.com/user-attachments/assets/1949bd27-a3b7-4d60-a2e9-d1d0141599e4)
