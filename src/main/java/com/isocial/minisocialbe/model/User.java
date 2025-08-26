package com.isocial.minisocialbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
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
    private String status;

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

}
