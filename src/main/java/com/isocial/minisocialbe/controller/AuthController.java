package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.service.AuthService;
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
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserCreateDto userCreateDto){
            User newUser = new User();
            newUser.setUsername(userCreateDto.getUsername());
            newUser.setEmail(userCreateDto.getEmail());
            newUser.setPassword(userCreateDto.getPassword());
            newUser.setFullName(userCreateDto.getFullName());
            newUser.setBio(userCreateDto.getBio());

            User registeredUser = authService.registerNewUser(newUser);

            if(registeredUser != null){
                return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Registration failed. Username or email already exist.", HttpStatus.BAD_REQUEST);
            }
        }
    }