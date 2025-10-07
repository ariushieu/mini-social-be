package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.user.ProfileResponseDto;
import com.isocial.minisocialbe.service.user.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> getProfileByUserId(@PathVariable("userId") Integer userId) {
        ProfileResponseDto profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

}
