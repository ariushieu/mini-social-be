package com.isocial.minisocialbe.dto.post;

import lombok.Data;

@Data
public class PostUpdateDto {
    private Integer id;
    private String content;
    private String mediaUrl;
    private String mediaType;
}
