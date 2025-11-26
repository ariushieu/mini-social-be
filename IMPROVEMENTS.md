# 📋 MINI-SOCIAL-BE - IMPROVEMENT ROADMAP

## 📊 Current Score: **7.5/10**

---

## 🔴 PRIORITY 1 - Critical (Must Do)

### 1. Add Unit Tests & Integration Tests

**Current Status**: ❌ No tests  
**Impact**: High - Production safety  
**Effort**: Medium

```java
// Example: LikeServiceTest.java
@SpringBootTest
@Transactional
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    void shouldLikePost_WhenValidRequest() {
        // Given
        Long userId = 1L;
        Long postId = 1L;

        // When
        likeService.likePost(userId, postId);

        // Then
        assertTrue(likeRepository.existsByUserIdAndTargetIdAndTargetType(
            userId, postId, TargetType.POST
        ));
    }

    @Test
    void shouldThrowException_WhenAlreadyLiked() {
        // Given
        Long userId = 1L;
        Long postId = 1L;
        likeService.likePost(userId, postId);

        // When & Then
        assertThrows(IllegalStateException.class, () ->
            likeService.likePost(userId, postId)
        );
    }
}
```

**Dependencies to add**:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>
```

---

### 2. Implement Profile Separation (Dev/Prod)

**Current Status**: ⚠️ Single configuration  
**Impact**: High - Security & Performance  
**Effort**: Low

**Create**:

- `application-dev.properties`
- `application-prod.properties`

```properties
# application-dev.properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG

# CORS - Allow all for development
cors.allowed-origins=http://localhost:5173,http://localhost:3000

# application-prod.properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.org.hibernate.SQL=WARN

# CORS - Specific origins only
cors.allowed-origins=${ALLOWED_ORIGINS}
```

**Update SecurityConfig.java**:

```java
@Value("${cors.allowed-origins}")
private String[] allowedOrigins;

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    Arrays.stream(allowedOrigins).forEach(config::addAllowedOrigin);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

---

### 3. Add Input Validation

**Current Status**: ⚠️ Minimal validation  
**Impact**: High - Security & Data integrity  
**Effort**: Low

```java
// dto/post/PostCreateDto.java
public class PostCreateDto {

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 5000, message = "Content must be between 1 and 5000 characters")
    private String content;

    @Size(max = 10, message = "Maximum 10 media files allowed")
    private List<MultipartFile> mediaFiles;
}

// dto/user/RegisterDto.java
public class RegisterDto {

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$",
             message = "Username must be 3-20 characters, alphanumeric and underscore only")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
             message = "Password must contain uppercase, lowercase, and digit")
    private String password;
}
```

**Update Controller**:

```java
@PostMapping
public ResponseEntity<?> createPost(@Valid @RequestBody PostCreateDto dto) {
    // Validation errors are automatically handled by GlobalExceptionHandler
    ...
}
```

---

### 4. Fix Field Injection (Use Constructor Injection)

**Current Status**: ⚠️ @Autowired field injection in SecurityConfig  
**Impact**: Medium - Best practice & Testability  
**Effort**: Low

```java
// SecurityConfig.java - BEFORE
@Autowired
private CustomUserDetailsService customUserDetailsService; // ❌

// SecurityConfig.java - AFTER
private final CustomUserDetailsService customUserDetailsService; // ✅

public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
    this.customUserDetailsService = customUserDetailsService;
}
```

---

## 🟡 PRIORITY 2 - Important (Should Do)

### 5. Add Logging

**Current Status**: ❌ No logging  
**Impact**: Medium - Debugging & Monitoring  
**Effort**: Low

```java
// Add to all Service classes
@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    @Transactional
    public void likePost(Long userId, Long postId) {
        log.info("User {} attempting to like post {}", userId, postId);

        try {
            // ... existing code ...
            log.info("User {} successfully liked post {}", userId, postId);
        } catch (Exception e) {
            log.error("Error liking post {} by user {}: {}", postId, userId, e.getMessage());
            throw e;
        }
    }
}
```

**Add logback-spring.xml**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

    <property name="LOG_FILE" value="logs/mini-social-be.log"/>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/mini-social-be.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>

    <logger name="com.isocial.minisocialbe" level="DEBUG"/>
</configuration>
```

---

### 6. Implement Pagination

**Current Status**: ❌ No pagination  
**Impact**: High - Performance with large datasets  
**Effort**: Medium

```java
// PostService.java
public Page<PostResponseDto> getAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable)
        .map(this::toDto);
}

public Page<PostResponseDto> getPostsByUserId(Long userId, Pageable pageable) {
    return postRepository.findByUserId(userId, pageable)
        .map(this::toDto);
}

// PostRepository.java
Page<Post> findByUserId(Long userId, Pageable pageable);

// PostController.java
@GetMapping
public ResponseEntity<Page<PostResponseDto>> getAllPosts(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "createdAt,desc") String[] sort
) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(
        Sort.Order.by(sort[0]).with(Sort.Direction.fromString(sort[1]))
    ));
    return ResponseEntity.ok(postService.getAllPosts(pageable));
}
```

---

### 7. Database Migration (Flyway)

**Current Status**: ⚠️ Using ddl-auto=update  
**Impact**: High - Production safety  
**Effort**: Medium

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

**application.properties**:

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

**Create migrations**:

```sql
-- src/main/resources/db/migration/V1__initial_schema.sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    ...
);

-- V2__add_likes_table.sql
CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

### 8. Improve Error Handling

**Current Status**: ⚠️ Using RuntimeException  
**Impact**: Medium - API consistency  
**Effort**: Low

```java
// Replace RuntimeException with specific exceptions
// PostService.java - BEFORE
throw new RuntimeException("User not found"); // ❌

// PostService.java - AFTER
throw new ResourceNotFoundException("User not found"); // ✅
throw new ResourceNotFoundException("Post not found with id: " + postId);
```

**Create custom exceptions**:

```java
// exception/UnauthorizedException.java
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

// Add to GlobalExceptionHandler
@ExceptionHandler(UnauthorizedException.class)
public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponse("Unauthorized", ex.getMessage()));
}
```

---

### 9. Standardize API Response Format

**Current Status**: ⚠️ Inconsistent responses  
**Impact**: Medium - API consistency  
**Effort**: Low

```java
// dto/ApiResponse.java
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}

// LikeController.java - Update all endpoints
@PostMapping
public ResponseEntity<ApiResponse<?>> likePost(...) {
    likeService.likePost(userDetails.getId(), postId);
    return ResponseEntity.ok(ApiResponse.success("Liked successfully", null));
}

@GetMapping
public ResponseEntity<ApiResponse<List<AuthorResponseDto>>> getUsersWhoLiked(...) {
    List<AuthorResponseDto> users = likeService.getUsersWhoLikedPost(postId);
    return ResponseEntity.ok(ApiResponse.success(users));
}
```

---

## 🟢 PRIORITY 3 - Nice to Have (Could Do)

### 10. Add Caching (Redis)

**Current Status**: ❌ No caching  
**Impact**: High - Performance  
**Effort**: Medium

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.cache.type=redis
spring.cache.redis.time-to-live=600000
```

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)))
            .build();
    }
}

// PostService.java
@Cacheable(value = "posts", key = "#postId")
public PostResponseDto getPost(Long postId) {
    // ...
}

@CacheEvict(value = "posts", key = "#postId")
public void deletePost(Long postId) {
    // ...
}
```

---

### 11. Add Rate Limiting

**Current Status**: ❌ No rate limiting  
**Impact**: High - Security  
**Effort**: Medium

```xml
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.1.0</version>
</dependency>
```

```java
// config/RateLimitInterceptor.java
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) {
        String key = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(key, k -> createBucket());

        if (bucket.tryConsume(1)) {
            return true;
        }

        response.setStatus(429); // Too Many Requests
        return false;
    }

    private Bucket createBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
            .build();
    }
}
```

---

### 12. Add Monitoring (Actuator + Prometheus)

**Current Status**: ❌ No monitoring  
**Impact**: High - Production observability  
**Effort**: Low

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

---

### 13. Add API Documentation

**Current Status**: ⚠️ Basic Swagger  
**Impact**: Medium - Developer experience  
**Effort**: Low

```java
// config/OpenApiConfig.java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Mini Social Network API")
                .version("1.0.0")
                .description("Social media backend API with posts, likes, follows, and comments")
                .contact(new Contact()
                    .name("Your Name")
                    .email("your.email@example.com")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}

// Add to Controllers
@Tag(name = "Like Management", description = "APIs for liking posts and comments")
@RestController
public class LikeController {

    @Operation(summary = "Like a post", description = "Add a like to a specific post")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully liked"),
        @ApiResponse(responseCode = "400", description = "Already liked"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping
    public ResponseEntity<?> likePost(...) { ... }
}
```

---

### 14. Implement Soft Delete

**Current Status**: ❌ Hard delete  
**Impact**: Medium - Data recovery  
**Effort**: Medium

```java
// Add to entities
@Entity
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Post {
    // ... existing fields ...

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
```

---

### 15. Add Email Templates

**Current Status**: ⚠️ Plain text emails  
**Impact**: Low - User experience  
**Effort**: Medium

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

```html
<!-- resources/templates/email/verification.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
  <body>
    <h1>Welcome to Mini Social!</h1>
    <p>Hi <span th:text="${username}"></span>,</p>
    <p>Please click the link below to verify your email:</p>
    <a th:href="${verificationLink}">Verify Email</a>
  </body>
</html>
```

---

## 📝 Code Quality Improvements

### Clean up commented code

```java
// User.java - Remove these
// @OneToMany(mappedBy = "follower")
// private List<Follow> following;

// FollowService.java - Remove
// private final FollowService followServiceProxy;
```

### Extract magic numbers to constants

```java
public class JwtConstants {
    public static final long ACCESS_TOKEN_VALIDITY = 30 * 60 * 1000; // 30 minutes
    public static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days
}
```

### Refactor long methods

```java
// PostService.createPost() - Extract media upload logic
private List<PostMedia> uploadMediaFiles(List<MultipartFile> files, Post post) {
    return files.stream()
        .map(file -> uploadSingleMedia(file, post))
        .collect(Collectors.toList());
}

private PostMedia uploadSingleMedia(MultipartFile file, Post post) {
    try {
        var result = cloudinaryService.uploadFile(file, "minisocial");
        String url = (String) result.get("secure_url");
        String publicId = (String) result.get("public_id");
        String type = determineMediaType(file);

        PostMedia media = new PostMedia(url, type, publicId);
        media.setPost(post);
        return media;
    } catch (IOException e) {
        throw new MediaUploadException("Failed to upload file", e);
    }
}
```

---

## 🎯 Target Score After Improvements

| Priority          | Tasks   | Score Impact | New Score |
| ----------------- | ------- | ------------ | --------- |
| P1 (Critical)     | 4 tasks | +1.5         | 9.0/10    |
| P2 (Important)    | 5 tasks | +0.5         | 9.5/10    |
| P3 (Nice to Have) | 6 tasks | +0.5         | 10/10     |

---

## 📅 Suggested Timeline

**Week 1-2**: Priority 1 (Critical)

- Unit tests
- Profile separation
- Input validation
- Constructor injection

**Week 3-4**: Priority 2 (Important)

- Logging
- Pagination
- Flyway migration
- Error handling
- API response standardization

**Week 5+**: Priority 3 (Optional)

- Caching
- Rate limiting
- Monitoring
- Documentation enhancements

---

## 🚀 Quick Wins (Can do in 1 day)

1. ✅ Fix field injection → constructor injection
2. ✅ Add @Slf4j to all services
3. ✅ Create profile configs (dev/prod)
4. ✅ Add validation annotations to DTOs
5. ✅ Clean up commented code
6. ✅ Extract magic numbers to constants
7. ✅ Standardize API response format

---

**Last Updated**: November 26, 2025  
**Next Review**: After P1 completion
