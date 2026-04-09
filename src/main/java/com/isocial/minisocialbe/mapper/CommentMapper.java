package com.isocial.minisocialbe.mapper;

import com.isocial.minisocialbe.dto.comment.CommentResponseDto;
import com.isocial.minisocialbe.dto.user.UserResponseDto;
import com.isocial.minisocialbe.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserMapper userMapper;

    public CommentResponseDto toDto(Comment comment){
        UserResponseDto userDto = userMapper.toLightweightDto(comment.getUser());

        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .user(userDto)
                .commentText(comment.getCommentText())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(null) // Không load replies tự động, dùng endpoint riêng
                .build();
    }
}
