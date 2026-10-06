# Project - Journal App Backend

A backend application built with Spring Boot for managing personal journal entries.
This project provides secure REST APIs for user authentication and journal management.

Features
User registration and authentication
Create, update, delete, and view journal entries
Secure API endpoints using Spring Security
Database integration for persistent storage
Docker support for containerized deployment
Health check endpoint
Tech Stack
Java
Spring Boot
Spring Security
Maven
MongoDB
Docker
Project Structure

src/main/java/com/MoonCrest/journalApp/

Controller → Handles API requests
Entity → Database models
Repository → Data access layer
Service → Business logic
Config → Security configuration
API Endpoints
User APIs
POST /user → Create user
GET /user → Get user details
PUT /user → Update user
DELETE /user → Delete user
Journal APIs
POST /journal → Create journal entry
GET /journal → Get all journal entries
GET /journal/{id} → Get journal by ID
PUT /journal/{id} → Update journal entry
DELETE /journal/{id} → Delete journal entry
Health Check
GET /health-check → Check server status
Setup Instructions
Clone the repository
git clone https://github.com/ChandrShekharJoshi/journal-app-backend.git
cd journal-app-backend
Configure application properties

Create your application.properties file:

spring.data.mongodb.uri=your_mongodb_connection
spring.data.mongodb.database=journaldb
Run the application
./mvnw spring-boot:run

or on Windows:

mvnw.cmd spring-boot:run
Docker Setup

Build Docker image:

docker build -t journal-app .

Run container:

docker run -p 8080:8080 journal-app
Branch Workflow
main → Stable production-ready code
dev → Active development branch
Future Improvements
JWT Authentication
Role-based authorization
API documentation with Swagger
Unit and integration testing
Deployment on cloud platforms
Author

Chandra Shekhar Joshi
MCA Student | Java Backend Developer
