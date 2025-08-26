package com.isocial.minisocialbe.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserCreateDto {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 12, message = "Username must be between 5 and 12 characters")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String fullName;
    private String bio;
}
