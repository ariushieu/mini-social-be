package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.service.post.IPostService;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;


    @PostMapping(value = "/create", consumes = {"multipart/form-data"}, produces = {"application/json"})
    public ResponseEntity<PostResponseDto> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("content") String content,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) throws IOException {
        PostResponseDto postResponseDto = postService.createPost(content, mediaFiles, userDetails);
        return ResponseEntity.ok(postResponseDto);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"}, produces = {"application/json"})
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestParam("content") String content,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) throws IOException {
        PostResponseDto updatedPost = postService.updatePost(id, content, mediaFiles);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.deletePost(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}