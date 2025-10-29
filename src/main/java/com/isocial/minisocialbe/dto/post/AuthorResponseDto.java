package com.isocial.minisocialbe.dto.post;

import com.isocial.minisocialbe.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String profilePicture;

}




