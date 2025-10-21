package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.exception.TokenRefreshException;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository  userRepository;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public Optional<User> findUserByRefreshToken(String token){
        return userRepository.findUserByRefreshToken(token);
    }

    public User verifyExpiration(User user){
        if (user.getRefreshTokenExpiryDate().compareTo(Instant.now()) < 0){
            user.setRefreshToken(null);
            user.setRefreshTokenExpiryDate(null);
            userRepository.save(user);

            throw new TokenRefreshException("Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return user;
    }

    public User rotateAndSaveNewToken(User user){
        String newRefreshToken = UUID.randomUUID().toString();
        Instant newExpiryDate = Instant.now().plusMillis(refreshTokenDurationMs);

        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiryDate(newExpiryDate);
        return userRepository.save(user);
    }
}
