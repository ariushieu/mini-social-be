package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.mapper.PostMapper;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.model.PostMedia;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.PostRepository;

import com.isocial.minisocialbe.service.storage.StorageService;
import com.isocial.minisocialbe.service.storage.UploadResult;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements IPostService{

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final PostMapper postMapper;


    @Override
    @Transactional
    public PostResponseDto createPost(String content, List<MultipartFile> mediaFiles, CustomUserDetails userDetails) throws IOException {
        User user = userDetails.getUser();

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            List<PostMedia> mediaList = mediaFiles.stream()
                    .map(file -> {
                        try {
                            UploadResult result = storageService.uploadFile(file, "minisocial");
                            PostMedia media = new PostMedia(result.url(), result.mediaType(), result.publicId());
                            media.setPost(post);
                            return media;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload file to Cloudinary", e);
                        }
                    })
                    .collect(Collectors.toList());

            post.setMedia(mediaList);
        }

        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long postId, String newContent, List<MultipartFile> newMediaFiles) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setContent(newContent);

        // Xóa media cũ (trên Cloudinary + DB)
        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            for (PostMedia oldMedia : post.getMedia()) {
                storageService.deleteFile(oldMedia.getPublicId(), oldMedia.getMediaType());
//                postMediaRepository.delete(oldMedia);
            }
            post.getMedia().clear();
        }

        if (newMediaFiles != null && !newMediaFiles.isEmpty()) {
            List<PostMedia> mediaList = newMediaFiles.stream()
                    .map(file -> {
                        try {
                            UploadResult result = storageService.uploadFile(file, "minisocial");
                            PostMedia media = new PostMedia(result.url(), result.mediaType(), result.publicId());
                            media.setPost(post);
                            return media;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload file to Cloudinary", e);
                        }
                    })
                    .collect(Collectors.toList());

            post.getMedia().addAll(mediaList);
        }

        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public void deletePost(Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }

        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            for (PostMedia media : post.getMedia()) {
                try {
                    storageService.deleteFile(media.getPublicId(), media.getMediaType());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete media from storage", e);
                }
            }
        }

        postRepository.delete(post);
    }

    @Override
    public List<PostResponseDto> getPostsByUserId(Long userId){
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }
}