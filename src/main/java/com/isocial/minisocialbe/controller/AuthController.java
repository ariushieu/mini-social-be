package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.user.LoginResponseDto;
import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.dto.user.UserLoginDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.service.auth.LoginService;
import com.isocial.minisocialbe.service.auth.RegisterService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCreateDto userCreateDto, HttpServletRequest request){

                try{
                    String siteURL = getSiteURL(request);
                    registerService.registerNewUser(userCreateDto,siteURL);
                    return new ResponseEntity<>("User registered successfully! Please check your email", HttpStatus.CREATED);
                }catch (MessagingException e){
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
    public String verifyUser(@RequestParam String code) {
        if (registerService.verify(code)) {
            return "Tài khoản của bạn đã được xác thực thành công. Bây giờ bạn có thể đăng nhập.";
        } else {
            return "Mã xác thực không hợp lệ hoặc tài khoản đã được kích hoạt.";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDto userLoginDto){
        LoginResponseDto response = loginService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
        return ResponseEntity.ok(response);
    }

    }