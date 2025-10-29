package com.isocial.minisocialbe.dto.user;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProfileResponseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profilePicture;
    private Integer followerCount;
    private Integer followingCount;
    private LocalDateTime joinDate;
    private LocalDateTime lastLogin;
    private List<PostResponseDto> posts;


    public ProfileResponseDto(Long id, String username, String fullName, String bio, String profilePicture, Integer followerCount, Integer followingCount, LocalDateTime joinDate, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.joinDate = joinDate;
        this.lastLogin = lastLogin;
    }
}

