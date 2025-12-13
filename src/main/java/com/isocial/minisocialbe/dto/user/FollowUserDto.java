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
public class FollowUserDto {
    private Long id;
    private String username;
    private String fullName;
    private String profilePicture;
    private String bio;
    private Boolean isFollowing; // Người dùng hiện tại có đang follow user này không
    private LocalDateTime followedAt;
}
