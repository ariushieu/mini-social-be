package com.isocial.minisocialbe.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 12, message = "Username must be between 5 and 12 characters")
    private String username;

    @NotBlank(message = "Full name cannot be empty")
    @Size(min = 2, max = 25, message = "Full name must be between 2 and 25 characters")
    private String fullName;

    @Size(max = 255, message = "Bio cannot exceed 255 characters")
    private String bio;

    private String profilePicture;
}
