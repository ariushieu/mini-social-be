package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPostService {
    PostResponseDto createPost(String content, List<MultipartFile> mediaFiles) throws IOException;
    PostResponseDto updatePost(Long postId, String newContent, List<MultipartFile> newMediaFiles) throws IOException;
    List<PostResponseDto> getPostsByUserId(Long userId);
}
