```markdown
# iSocial: A Mini Social Network

iSocial is a mini social network application that allows users to create posts, interact with content (like, comment), follow other users, and receive notifications. [cite_start]This project is built to showcase a full-stack application development. [cite: 3, 4]

---

## 💡 Core Features & Technologies

### [cite_start]Features [cite: 9]
* [cite_start]**Account Management**: Users can register, log in, manage their personal profiles, and are assigned roles (user, admin). [cite: 11]
* [cite_start]**Posting**: Users can create posts with text and/or images. [cite: 12]
* **Interaction**:
    * [cite_start]**Likes**: Like posts or comments. [cite: 14]
    * [cite_start]**Comments**: Comment on posts or reply to other comments. [cite: 15]
* [cite_start]**Connections**: Follow other users to see their latest posts on your news feed. [cite: 16]
* [cite_start]**Notifications**: Get notified about new interactions like likes, comments, or follows. [cite: 17]
* [cite_start]**News Feed**: A main feed that displays the latest posts from the users you follow. [cite: 18]

### [cite_start]Technologies [cite: 5]
* [cite_start]**Frontend**: React, TypeScript, Vite. [cite: 6]
* [cite_start]**Backend**: Spring Boot (Java). [cite: 7]
* [cite_start]**Database**: MySQL. [cite: 8]

---

## 📂 Project Structure

[cite_start]The backend project follows a logical and modular structure to ensure maintainability and scalability. [cite: 27]

```

com.isocial.isocial
[cite\_start]├── controller        // REST APIs for handling HTTP requests [cite: 30]
│   ├── AuthController.java
│   ├── PostController.java
│   └── UserController.java
│
[cite\_start]├── model             // Entity classes mapped to database tables [cite: 35]
│   ├── User.java
│   ├── Post.java
│   ├── Comment.java
│   └── Notification.java
│
[cite\_start]├── repository        // Interfaces for data access using Spring Data JPA [cite: 40]
│   ├── UserRepository.java
│   ├── PostRepository.java
│   └── CommentRepository.java
│
[cite\_start]├── service           // Business logic layer [cite: 44]
│   ├── UserService.java
│   ├── PostService.java
│   └── NotificationService.java
│
[cite\_start]├── dto               // (Optional) Data Transfer Objects for data exchange [cite: 49]
[cite\_start]└── config            // Configuration files (e.g., security, CORS) [cite: 51]

```

---

## 🛠️ API Endpoints

[cite_start]Here are the key API endpoints for the application: [cite: 52]

### [cite_start]Account Management [cite: 54]
* [cite_start]**Register**: `POST /api/auth/register` [cite: 56]
    * [cite_start]**Logic**: Validates user data, hashes the password, and saves the user to the database. [cite: 57]
* [cite_start]**Login**: `POST /api/auth/login` [cite: 59]
    * [cite_start]**Logic**: Authenticates the user and returns an authentication token. [cite: 60]

### [cite_start]Posts & Interactions [cite: 61]
* [cite_start]**Create Post**: `POST /api/posts` [cite: 63]
    * [cite_start]**Logic**: Saves post content and images to the `posts` and `post_media` tables. [cite: 64]
* [cite_start]**Get News Feed**: `GET /api/posts/feed` [cite: 66]
    * [cite_start]**Logic**: Retrieves posts from followed users, typically via a stored procedure. [cite: 67, 68]
* [cite_start]**Like Post/Comment**: `POST /api/likes` [cite: 70]
    * **Logic**: Inserts a new record into the `likes` table. [cite_start]A database trigger automatically updates the `like_count` and generates a notification. [cite: 71, 72, 73]
* [cite_start]**Comment on Post**: `POST /api/comments` [cite: 75]
    * **Logic**: Inserts a new record into the `comments` table. [cite_start]A trigger updates the `comment_count` on the `posts` table. [cite: 76, 77, 78]
* [cite_start]**Reply to Comment**: `POST /api/comments/reply` [cite: 80]
    * [cite_start]**Logic**: This involves two separate SQL commands: an `INSERT` to add the new comment and an `UPDATE` to increment the `reply_count` of the parent comment. [cite: 22, 81, 82, 83]

### [cite_start]Connections & Notifications [cite: 84]
* [cite_start]**Follow User**: `POST /api/follows/{userId}` [cite: 86]
    * **Logic**: Inserts a new record into the `follows` table. [cite_start]A trigger automatically updates the `follower_count` and `following_count`. [cite: 87, 88]
* [cite_start]**Get Notifications**: `GET /api/notifications` [cite: 90]
    * [cite_start]**Logic**: Queries the `notifications` table to retrieve a list of notifications for the current user. [cite: 91, 92]

---

## ⚙️ Database Operations

[cite_start]The project utilizes MySQL as its database. [cite: 8] [cite_start]For optimal performance, some operations, like replying to a comment, are handled by executing two separate SQL commands on the backend to ensure data integrity. [cite: 20, 22]

[cite_start]**Example: Replying to a comment (parent with ID 3) [cite: 25]**
1.  [cite_start]`INSERT INTO comments (post_id, user_id, parent_comment_id, comment_text) VALUES (1, 1, 3, 'Thanks, Bob!');` [cite: 23]
2.  [cite_start]`UPDATE comments SET reply_count = reply_count + 1 WHERE id = 3;` [cite: 24]

[cite_start]For the news feed, a stored procedure (`GetNewsFeed`) is used to efficiently retrieve posts from followed users. [cite: 67, 68]
```
