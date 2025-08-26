package com.isocial.minisocialbe.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUpdateDto {
    @NotNull(message = "Comment ID is required for update")
    private Integer id;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String commentText;
}
