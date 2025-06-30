# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Architecture

This is a microservices-based pharmacy management system with Spring Boot backend services and Angular frontend:

### Backend Services (Spring Boot)
- **ms-config-server**: Configuration server running on port 7071
- **ms-registry-server**: Eureka service registry
- **ms-auth**: Authentication service with JWT tokens
- **ms-gateway-service**: API Gateway with authentication filters
- **ms_catalogo**: Catalog service managing categories
- **ms_producto**: Product service with Feign client to catalog service

### Frontend
- **dad-front**: Angular 20 application consuming the microservices

## Common Development Commands

### Frontend (Angular)
```bash
cd dad-front
npm install                    # Install dependencies
ng serve                      # Start development server (http://localhost:4200)
ng build                      # Build for production
ng test                       # Run unit tests with Karma
ng generate component <name>  # Generate new component
```

### Backend (Spring Boot)
```bash
# For any microservice directory
./mvnw spring-boot:run        # Run service locally
./mvnw clean install         # Build and install dependencies
./mvnw test                   # Run tests
```

## Service Startup Order

1. Start ms-config-server first (port 7071)
2. Start ms-registry-server (Eureka)
3. Start other services (ms-auth, ms-gateway-service, ms_catalogo, ms_producto)
4. Start Angular frontend

## Key Configuration

- **Config Server**: Pulls configuration from `config-data/` directory
- **Service Registry**: Eureka server for service discovery
- **Authentication**: JWT-based with Spring Security
- **Database**: H2 for catalog service, MySQL for auth service
- **API Gateway**: Routes requests and handles authentication
- **Frontend API**: Connects to gateway at http://localhost:8085

## Architecture Patterns

- **Service Discovery**: Eureka for service registration/discovery
- **Configuration Management**: Spring Cloud Config Server
- **API Gateway**: Centralized entry point with authentication
- **Inter-service Communication**: Feign clients for service-to-service calls
- **Security**: JWT tokens with Spring Security
- **Data Access**: JPA/Hibernate with repository pattern

## Testing

- **Backend**: Maven Surefire plugin for unit tests
- **Frontend**: Karma + Jasmine for unit tests