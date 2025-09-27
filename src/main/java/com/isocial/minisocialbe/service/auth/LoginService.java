package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.dto.user.LoginResponseDto;
import com.isocial.minisocialbe.dto.user.UserResponseDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
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
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public LoginResponseDto login(String email, String rawPassword) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = customUserDetails.getUser();

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails.getUsername());

        user.setLastLogin(LocalDateTime.now());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

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
                .refreshToken(refreshToken)
                .user(userResponseDto)
                .build();

    }
}
