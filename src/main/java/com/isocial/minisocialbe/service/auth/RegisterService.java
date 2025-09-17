package com.isocial.minisocialbe.service.auth;

import com.isocial.minisocialbe.dto.user.UserCreateDto;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.UserRepository;
import com.isocial.minisocialbe.service.validate.RegisterValidation;
import jakarta.mail.MessagingException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegisterValidation registerValidation;

    @Autowired
    private MailService mailService;

    public User registerNewUser(UserCreateDto userCreateDto, String siteURL) throws MessagingException, UnsupportedEncodingException {
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

        mailService.sendVerificationMail(savedUser, siteURL);
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
