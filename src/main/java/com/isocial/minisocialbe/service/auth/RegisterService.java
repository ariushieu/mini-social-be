package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(UserCreateDto userCreateDto){
        if(userRepository.existsByUsername(userCreateDto.getUsername()) || userRepository.existsByEmail(userCreateDto.getEmail())){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userCreateDto.getUsername());
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setPassword(userCreateDto.getPassword());
        newUser.setFullName(userCreateDto.getFullName());
        newUser.setBio(userCreateDto.getBio());

        newUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        return userRepository.save(newUser);
    }




}
