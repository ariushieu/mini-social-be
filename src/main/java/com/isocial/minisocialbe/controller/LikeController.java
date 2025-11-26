package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.service.post.LikeService;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getUsersWhoLiked(@PathVariable Long postId) {
        List<AuthorResponseDto> users = likeService.getUsersWhoLikedPost(postId);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> likePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            likeService.likePost(userDetails.getId(), postId);
            return ResponseEntity.ok("Like successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            likeService.unlikePost(userDetails.getId(), postId);
            return ResponseEntity.ok("Unlike successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLikeStatus(@PathVariable Long postId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean isLiked = likeService.isPostLikedByUser(userDetails.getId(), postId);
        return ResponseEntity.ok(isLiked);
    }
}
