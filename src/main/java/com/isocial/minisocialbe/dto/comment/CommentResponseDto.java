package com.isocial.minisocialbe.dto.comment;

import com.isocial.minisocialbe.dto.user.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private UserResponseDto user;
    private String commentText;
    private Integer likeCount;
    private Integer replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponseDto> replies;
}
