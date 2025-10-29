package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping(value = "/create", consumes = {"multipart/form-data"}, produces = {"application/json"})
    public ResponseEntity<PostResponseDto> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) throws IOException {
        PostResponseDto postResponseDto = postService.createPost(content, mediaFiles);
        return ResponseEntity.ok(postResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestParam("content") String content,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) throws IOException {
        Post updatedPost = postService.updatePost(id, content, mediaFiles);
        return ResponseEntity.ok(updatedPost);
    }
}