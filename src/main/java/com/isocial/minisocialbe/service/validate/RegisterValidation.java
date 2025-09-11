package com.isocial.minisocialbe.service.validate;

import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.exception.DuplicateUserException;
import com.isocial.minisocialbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterValidation {
    @Autowired
    private UserRepository userRepository;

    public void validateUserCreation(UserCreateDto userCreateDto){
        if (userRepository.existsByUsername(userCreateDto.getUsername())){
            throw new DuplicateUserException("Username already exists");
        }
        if (userRepository.existsByEmail(userCreateDto.getEmail())){
            throw new DuplicateUserException("Email already exists");
        }
    }
}
