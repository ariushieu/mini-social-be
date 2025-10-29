package com.isocial.minisocialbe.dto.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String content;
    private AuthorResponseDto user;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private List<MediaResponseDto> media;
}
