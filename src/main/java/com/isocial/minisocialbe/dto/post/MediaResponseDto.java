package com.isocial.minisocialbe.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponseDto {
    private Integer id;
    private String mediaUrl;
    private String mediaType;

}
