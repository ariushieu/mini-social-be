package com.isocial.minisocialbe.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentCreateDto {
    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Post ID is required")
    private Integer postId;

    private Integer parentCommentId;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String commentText;
}
