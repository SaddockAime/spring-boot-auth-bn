# Spring Boot Auth Service

A complete authentication service built with Spring Boot that provides:

- JWT-based authentication
- OAuth2 login with Google
- Role-based authorization
- User management

## Technologies Used

- Java 17
- Spring Boot 3.4.5
- Spring Security
- JWT (JSON Web Tokens)
- OAuth2
- PostgreSQL
- JPA / Hibernate
- Maven

## Prerequisites

- JDK 17 or higher
- PostgreSQL
- Maven (or use the included Maven wrapper)

## Database Setup

1. Create a PostgreSQL database named `leave_management`
2. Update the database configuration in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/leave_management
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Building and Running the Application

### Using Maven Wrapper

Build the project:
```bash
./mvnw clean package -DskipTests
```

Run the application:
```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Using Maven

Build the project:
```bash
mvn clean package -DskipTests
```

Run the application:
```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

The application will run on http://localhost:8081 by default.

## Features

### Authentication Endpoints

- **POST /api/auth/signup**: Register a new user
- **POST /api/auth/login**: Authenticate a user and receive JWT
- **GET /api/auth/validate**: Validate JWT token

### User Management Endpoints

- **GET /api/users**: Get all users (ADMIN only)
- **GET /api/users/{id}**: Get user by ID (ADMIN or self)
- **PUT /api/users/{id}/roles**: Update user roles (ADMIN only)

### OAuth2 Authentication

The application supports Google OAuth2 login. Configure your Google OAuth2 credentials in `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=your-client-id
spring.security.oauth2.client.registration.google.client-secret=your-client-secret
```

## Default Accounts

The application automatically creates a default admin account:
- Email: saddock2000@gmail.com
- Password: admin123

## Project Structure

- **Controller Layer**: Handles HTTP requests and responses
- **Security Layer**: JWT configuration, OAuth2 setup
- **Service Layer**: Business logic
- **Repository Layer**: Data access
- **Model Layer**: Entity definitions
- **DTO Layer**: Data transfer objects

## Security Configuration

This project implements a comprehensive security configuration:

- JWT authentication
- OAuth2 social login
- CORS configuration
- Role-based access control
- Password encryption using BCrypt