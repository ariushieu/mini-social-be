package com.isocial.minisocialbe.controller;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

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
            @PathVariable Integer id,
            @RequestParam("content") String content,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) throws IOException {
        Post updatedPost = postService.updatePost(id, content, mediaFiles);
        return ResponseEntity.ok(updatedPost);
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Integer userId) {
//        List<Post> posts = postService.getPostByUserId(userId);
//
//        if (posts.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(posts, HttpStatus.OK);
//    }
}