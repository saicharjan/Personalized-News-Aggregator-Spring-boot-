# Personalized News Aggregator

A Spring Boot-based RESTful application that delivers personalized news feeds based on user preferences.

## Features

- User Authentication with JWT
- Real-time news updates using NewsAPI integration
- Personalized news feed based on user preferences
- Search functionality for articles
- WebSocket support for live updates
- Caching for improved performance
- Pagination for large news feeds
- Secure endpoints using Spring Security

## Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- NewsAPI Key (Sign up at https://newsapi.org)

## Configuration

1. Update `application.properties` with your database credentials
2. Add your NewsAPI key in `application.properties`
3. Configure JWT secret key in `application.properties`

## Building and Running

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## API Endpoints

### Authentication
- POST /api/auth/signup - Register new user
- POST /api/auth/login - User login

### News
- GET /api/news - Get personalized news feed
- GET /api/news/search - Search articles
- GET /api/news/categories - Get available categories

### User Preferences
- GET /api/preferences - Get user preferences
- PUT /api/preferences - Update user preferences

## Security

All endpoints except authentication endpoints require JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## WebSocket Connection

Connect to WebSocket for real-time updates:
```
ws://localhost:8080/api/ws
```
