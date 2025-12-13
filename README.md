# Mini Social Backend

A social media backend API built with Spring Boot 3.5, featuring user authentication, posts, comments, likes, and follow system.

## Tech Stack

- Java 17
- Spring Boot 3.5.5
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Cloudinary (media storage)
- Springdoc OpenAPI (Swagger)

## Requirements

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Cloudinary account

## Setup

1. Clone the repository

2. Create `.env` file in root directory:

```
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
SECRET_KEY=your_jwt_secret_key
GMAIL_USERNAME=your_gmail
GMAIL_PASSWORD=your_gmail_app_password
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

3. Create MySQL database:

```sql
CREATE DATABASE isocial_network;
```

4. Run the application:

```bash
mvn spring-boot:run
```

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication

```
POST   /api/auth/register     - Register new user
POST   /api/auth/login        - Login
POST   /api/auth/refresh      - Refresh access token
GET    /api/auth/verify       - Verify email
```

### Profile

```
POST   /api/profile/{userId}  - Get user profile
```

### Posts

```
POST   /api/posts/create      - Create post (multipart/form-data)
PUT    /api/posts/{id}        - Update post
```

### Comments

```
POST   /api/v1/posts/{postId}/comments              - Create comment
GET    /api/v1/posts/{postId}/comments              - Get comments by post
GET    /api/v1/posts/{postId}/comments/{id}/replies - Get replies
PUT    /api/v1/posts/{postId}/comments/{id}         - Update comment
DELETE /api/v1/posts/{postId}/comments/{id}         - Delete comment
```

### Post Likes

```
POST   /api/v1/posts/{postId}/likes       - Like post
DELETE /api/v1/posts/{postId}/likes       - Unlike post
GET    /api/v1/posts/{postId}/likes       - Get users who liked
GET    /api/v1/posts/{postId}/likes/check - Check like status
```

### Comment Likes

```
POST   /api/v1/comments/{commentId}/likes       - Like comment
DELETE /api/v1/comments/{commentId}/likes       - Unlike comment
GET    /api/v1/comments/{commentId}/likes       - Get users who liked
GET    /api/v1/comments/{commentId}/likes/check - Check like status
```

### Follow

```
POST   /api/v1/follows/{userId}             - Follow user
DELETE /api/v1/follows/{userId}             - Unfollow user
GET    /api/v1/follows/{userId}/followers   - Get followers (paginated)
GET    /api/v1/follows/{userId}/following   - Get following (paginated)
GET    /api/v1/follows/{userId}/stats       - Get follow stats
GET    /api/v1/follows/check/{targetUserId} - Check follow status
```

## Request Examples

### Register

```
POST /api/auth/register
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Password123",
  "fullName": "John Doe",
  "bio": "Hello world"
}
```

### Login

```
POST /api/auth/login
{
  "email": "john@example.com",
  "password": "Password123"
}
```

### Refresh Token

```
POST /api/auth/refresh
{
  "refreshToken": "your_refresh_token"
}
```

### Create Post

```
POST /api/posts/create
Content-Type: multipart/form-data

content: "Hello world"
mediaFiles: [file1.jpg, file2.png]
```

### Create Comment

```
POST /api/v1/posts/1/comments
{
  "commentText": "Nice post!",
  "parentCommentId": null
}
```

### Create Reply

```
POST /api/v1/posts/1/comments
{
  "commentText": "I agree!",
  "parentCommentId": 5
}
```

## Authentication

All protected endpoints require JWT token in Authorization header:

```
Authorization: Bearer <access_token>
```

## License

MIT
