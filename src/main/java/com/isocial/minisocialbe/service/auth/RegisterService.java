package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import com.isocial.minisocialbe.service.validate.RegisterValidation;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RegisterValidation registerValidation;

    private final ResendMailService resendMailService;

    public User registerNewUser(UserCreateDto userCreateDto, String siteURL) {
        registerValidation.validateUserCreation(userCreateDto);

        User newUser = new User();
        newUser.setUsername(userCreateDto.getUsername());
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setPassword(userCreateDto.getPassword());
        newUser.setFullName(userCreateDto.getFullName());
        newUser.setBio(userCreateDto.getBio());

        String randomCode = RandomString.make(64);
        newUser.setVerificationCode(randomCode);

        //encode password
        newUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User savedUser = userRepository.save(newUser);

        resendMailService.sendVerificationMail(savedUser);

        return savedUser;
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }
}
