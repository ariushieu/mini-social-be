package com.isocial.minisocialbe.dto.post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostUpdateDto {
    private Integer id;
    private String content;
    private String mediaUrl;
    private String mediaType;
}
