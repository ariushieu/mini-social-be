package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.user.LoginResponseDto;
import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.dto.user.UserLoginDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.service.auth.LoginService;
import com.isocial.minisocialbe.service.auth.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCreateDto userCreateDto){

            User registeredUser = registerService.registerNewUser(userCreateDto);
            if(registeredUser != null){
                return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Registration failed. Username or email already exist.", HttpStatus.BAD_REQUEST);
            }
        }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDto userLoginDto){
        LoginResponseDto response = loginService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
        return ResponseEntity.ok(response);
    }

    }