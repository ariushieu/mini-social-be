package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.dto.user.LoginResponseDto;
import com.isocial.minisocialbe.dto.user.UserResponseDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDto login(String email, String rawPassword) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = customUserDetails.getUser();

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        user = refreshTokenService.rotateAndSaveNewToken(user);

        String accessToken = jwtService.generateAccessToken(customUserDetails);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .role(user.getRole())
                .profilePicture(user.getProfilePicture())
                .followerCount(user.getFollowerCount())
                .followingCount(user.getFollowingCount())
                .joinDate(user.getJoinDate())
                .lastLogin(user.getLastLogin())
                .build();

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(user.getRefreshToken())
                .user(userResponseDto)
                .build();
    }
}

