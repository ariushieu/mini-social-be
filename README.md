
POST /api/v1/posts/{postId}/likes - Like bài viết
DELETE /api/v1/posts/{postId}/likes - Unlike bài viết
GET /api/v1/posts/{postId}/likes - Lấy danh sách user đã like
GET    /api/v1/posts/{postId}/likes/check  - Check user hiện tại đã like chưa


POST /api/auth/login
POST /api/auth/refresh:
    {
            "refreshToken": ""
    }
POST /api/profile/{id}
POST /api/v1/follows/{id}
POST /api/auth/register:
    {
        "username": "hieun122",
        "email": "hieun@example.com",
        "password": "Hieu123",
        "fullName": "Nguyen Quoc Hieu",
        "bio": "Testing API register 10/9."
    }

DELETE /api/v1/follows/{id}

POST /api/posts
    {
        content: "", 
        mediaFiles
    }