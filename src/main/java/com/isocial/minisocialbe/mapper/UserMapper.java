package com.isocial.minisocialbe.mapper;


import com.isocial.minisocialbe.dto.user.UserResponseDto;
import com.isocial.minisocialbe.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .role(user.getRole())
                .profilePicture(user.getProfilePicture())
                .followerCount(user.getFollowerCount())
                .followingCount(user.getFollowingCount())
                .joinDate(user.getJoinDate())
                .lastLogin(user.getLastLogin())
                .build();
    }

    public UserResponseDto toLightweightDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
