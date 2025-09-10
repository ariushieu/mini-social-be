package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.dto.user.LoginResponseDto;
import com.isocial.minisocialbe.dto.user.UserResponseDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public boolean checkPassword(String rawPassword, String encodedPassword){
//        return passwordEncoder.matches(rawPassword, encodedPassword);
//    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public LoginResponseDto login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No found user"));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

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

        }catch (AuthenticationException e){
            throw new RuntimeException("Invalid email or password.", e);
        }
    }
}
