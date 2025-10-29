package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.repository.FollowRepository;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import com.isocial.minisocialbe.service.user.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<?> followUser(@PathVariable Long followingId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long followerId = userDetails.getId();
            followService.followUser(followerId, followingId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đã theo dõi thành công.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followingId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long followerId = userDetails.getId();
            followService.unfollowUser(followerId, followingId);
            return ResponseEntity.ok("Đã hủy theo dõi thành công.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
