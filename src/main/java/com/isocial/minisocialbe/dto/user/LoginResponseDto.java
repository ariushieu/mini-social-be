package com.isocial.minisocialbe.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto user;
}
