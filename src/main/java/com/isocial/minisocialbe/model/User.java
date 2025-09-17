package com.isocial.minisocialbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    private String bio;

    @Column(name = "profile_picture")
    private String profilePicture;

    private String role;

    @Column(name = "follower_count")
    private Integer followerCount;

    @Column(name = "following_count")
    private Integer followingCount;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "follower")
    private List<Follow> following;

    @OneToMany(mappedBy = "following")
    private List<Follow> followers;

    @Column(name = "is_enabled")
    private boolean isEnabled = false;

    @Column(name = "is_locked")
    private boolean isLocked = false;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    //create default value in first time sign up
    @PrePersist
    protected void onCreate() {
        if (joinDate == null) {
            this.joinDate = LocalDateTime.now();
        }
        if (followerCount == null) {
            this.followerCount = 0;
        }
        if (followingCount == null) {
            this.followingCount = 0;
        }
        if (role == null) {
            this.role = "user";
        }
        if (lastLogin == null) {
            this.lastLogin = LocalDateTime.now();
        }
    }

}
