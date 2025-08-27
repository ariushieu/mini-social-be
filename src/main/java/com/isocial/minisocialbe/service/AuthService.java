package com.isocial.minisocialbe.service;

import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            return null;
        }

        if (userRepository.existsByEmail(user.getEmail())){
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
