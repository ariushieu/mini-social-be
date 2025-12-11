package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.repository.LikeRepository;
import com.isocial.minisocialbe.service.post.LikeService;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments/{commentId}/likes")
@RequiredArgsConstructor
public class LikeCommentController {
    private final LikeService  likeService;

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getUsersWhoLikedComments(@PathVariable("commentId") Long commentId) {
        List<AuthorResponseDto> users = likeService.getUsersWhoLikedComment(commentId);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> likeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            likeService.likeComment(userDetails.getId(), commentId);
            return ResponseEntity.ok("Liked comment successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unlikeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            likeService.unlikeComment(userDetails.getId(), commentId);
            return ResponseEntity.ok("Unliked comment successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLikeStatus(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boolean isLiked = likeService.isCommentLikedByUser(userDetails.getId(), commentId);
        return ResponseEntity.ok(isLiked);
    }
}
