package com.isocial.minisocialbe.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class PostCreateDto {
    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    private String content;

    private List<MediaCreateDto> mediaList;
}
