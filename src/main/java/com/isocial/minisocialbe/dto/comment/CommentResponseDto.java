package com.isocial.minisocialbe.dto.comment;

import com.isocial.minisocialbe.dto.user.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponseDto {
    private Integer id;
    private Integer postId;
    private UserResponseDto user;
    private String commentText;
    private Integer likeCount;
    private Integer replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponseDto> replies;
}
