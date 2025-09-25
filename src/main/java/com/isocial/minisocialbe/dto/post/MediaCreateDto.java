package com.isocial.minisocialbe.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class MediaCreateDto {
    @NotNull(message = "Media file cannot be null")
    private MultipartFile mediaFile;

    @NotBlank(message = "Meida type cannot be empty")
    private String mediaType;
}
