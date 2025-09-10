package com.isocial.minisocialbe.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret-key}")
    private String secretString;

    private Key SECRET_KEY;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(secretString);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    public String generateAccessToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("NO_ROLE");

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
        return claimsJws.getBody().getSubject();
    }
}
