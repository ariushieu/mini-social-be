package com.isocial.minisocialbe.dto.post;

import com.isocial.minisocialbe.dto.user.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {
    private Integer id;
    private String content;
    private UserResponseDto user;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;


    private String mediaUrl;
    private String mediaType;
}
