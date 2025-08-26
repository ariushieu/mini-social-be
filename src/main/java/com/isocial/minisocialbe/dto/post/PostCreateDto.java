package com.isocial.minisocialbe.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostCreateDto {
    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    private String content;

    private String mediaUrl;
    private String mediaType;
}
