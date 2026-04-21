package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPostService {
    PostResponseDto createPost(String content, List<MultipartFile> mediaFiles, CustomUserDetails userDetails) throws IOException;
    PostResponseDto updatePost(Long postId, String newContent, List<MultipartFile> newMediaFiles) throws IOException;
    List<PostResponseDto> getPostsByUserId(Long userId);
    void deletePost(Long postId, CustomUserDetails userDetails);
}
