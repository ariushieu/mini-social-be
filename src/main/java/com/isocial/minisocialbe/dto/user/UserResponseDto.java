package com.isocial.minisocialbe.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String role;
    private String profilePicture;
    private Integer followerCount;
    private Integer followingCount;
    private LocalDateTime joinDate;
    private LocalDateTime lastLogin;
}
