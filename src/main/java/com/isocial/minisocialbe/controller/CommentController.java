package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.comment.CommentCreateDto;
import com.isocial.minisocialbe.dto.comment.CommentResponseDto;
import com.isocial.minisocialbe.dto.comment.CommentUpdateDto;
import com.isocial.minisocialbe.service.post.CommentService;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        dto.setPostId(postId);
        dto.setUserId(userDetails.getId());

        CommentResponseDto response = commentService.createComment(dto, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentResponseDto>> getRepliesByCommentId(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        List<CommentResponseDto> replies = commentService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        dto.setId(commentId);
        CommentResponseDto response = commentService.updateComment(dto, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.deleteComment(commentId, userDetails.getId());
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
