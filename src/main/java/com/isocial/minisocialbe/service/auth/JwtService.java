package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.service.user.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret-key}")
    private String secretString;

    private Key SECRET_KEY;


    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(secretString);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

//    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;
//    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    @Value("${app.jwtAccessExpirationMs}")
    private Long accessTokenDurationMs;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public String generateAccessToken(CustomUserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("NO_ROLE");

        // 2. Tạo Map để chứa tất cả các claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userDetails.getUser().getId());

        // 3. Sử dụng claims() để thêm tất cả claim vào builder
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenDurationMs))
                .signWith((javax.crypto.SecretKey) SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenDurationMs))
//                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
//                .compact();
        return UUID.randomUUID().toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((javax.crypto.SecretKey) SECRET_KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
