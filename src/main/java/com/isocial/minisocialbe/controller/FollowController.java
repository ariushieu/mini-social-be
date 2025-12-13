package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.user.FollowStatsDto;
import com.isocial.minisocialbe.dto.user.FollowUserDto;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import com.isocial.minisocialbe.service.user.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Đã theo dõi thành công."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followingId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long followerId = userDetails.getId();
            followService.unfollowUser(followerId, followingId);
            return ResponseEntity.ok(Map.of("message", "Đã hủy theo dõi thành công."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long currentUserId = userDetails != null ? userDetails.getId() : null;
            Pageable pageable = PageRequest.of(page, size);
            Page<FollowUserDto> followers = followService.getFollowers(userId, currentUserId, pageable);
            return ResponseEntity.ok(followers);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long currentUserId = userDetails != null ? userDetails.getId() : null;
            Pageable pageable = PageRequest.of(page, size);
            Page<FollowUserDto> following = followService.getFollowing(userId, currentUserId, pageable);
            return ResponseEntity.ok(following);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<?> getFollowStats(@PathVariable Long userId) {
        try {
            FollowStatsDto stats = followService.getFollowStats(userId);
            return ResponseEntity.ok(stats);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/check/{targetUserId}")
    public ResponseEntity<?> checkFollowStatus(@PathVariable Long targetUserId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long currentUserId = userDetails.getId();
        boolean isFollowing = followService.isFollowing(currentUserId, targetUserId);
        boolean isFollowedBy = followService.isFollowing(targetUserId, currentUserId);
        boolean isMutual = isFollowing && isFollowedBy;

        return ResponseEntity.ok(Map.of(
                "isFollowing", isFollowing,
                "isFollowedBy", isFollowedBy,
                "isMutual", isMutual
        ));
    }
}
