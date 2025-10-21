package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.auth.TokenRefreshRequest;
import com.isocial.minisocialbe.dto.auth.TokenRefreshResponse;
import com.isocial.minisocialbe.dto.user.LoginResponseDto;
import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.dto.user.UserLoginDto;
import com.isocial.minisocialbe.exception.TokenRefreshException;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.service.auth.*;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor //auto inject constructor
public class AuthController {

    private final RegisterService registerService;
    private final LoginService loginService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

     @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCreateDto userCreateDto,
                                               HttpServletRequest request) {
        try {
            String siteURL = getSiteURL(request);
            registerService.registerNewUser(userCreateDto, siteURL);
            return new ResponseEntity<>("User registered successfully! Please check your email", HttpStatus.CREATED);
        } catch (MessagingException e) {
            return new ResponseEntity<>("Đăng ký thành công nhưng không thể gửi email xác thực.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>("Lỗi khi xử lý email.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Đăng ký thất bại: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyUser(@RequestParam String code) {
        Map<String, Object> response = new HashMap<>();
        if (registerService.verify(code)) {
            response.put("success", true);
            response.put("message", "Tài khoản đã được xác thực thành công. Bạn sẽ chuyển đến trang đăng nhập sau 5 giây.");
        } else {
            response.put("success", false);
            response.put("message", "Mã xác thực không hợp lệ hoặc tài khoản đã được kích hoạt.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        LoginResponseDto response = loginService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        User user = refreshTokenService.findUserByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new TokenRefreshException("Refresh token không hợp lệ hoặc không tồn tại."));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        refreshTokenService.rotateAndSaveNewToken(user);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, user.getRefreshToken()));
    }
}
