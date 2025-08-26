package com.isocial.minisocialbe.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profilePicture;
    private Integer followerCount;
    private Integer followingCount;
    private LocalDateTime joinDate;
    private LocalDateTime lastLogin;
}
