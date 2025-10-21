package com.isocial.minisocialbe.dto.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
