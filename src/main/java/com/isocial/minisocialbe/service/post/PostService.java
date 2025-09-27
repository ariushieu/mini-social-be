package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.post.MediaResponseDto;
import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.model.PostMedia;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.PostMediaRepository;
import com.isocial.minisocialbe.repository.PostRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import com.isocial.minisocialbe.service.CloudinaryService;
import com.isocial.minisocialbe.service.user.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       PostMediaRepository postMediaRepository,
                       CloudinaryService cloudinaryService,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMediaRepository = postMediaRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }

    private PostResponseDto toDto(Post post) {
        List<MediaResponseDto> mediaDtos = post.getMedia() != null
                ? post.getMedia().stream()
                .map(m -> new MediaResponseDto(m.getMediaUrl(), m.getMediaType()))
                .collect(Collectors.toList())
                : List.of();

        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .user(AuthorResponseDto.builder()
                        .id(post.getUser().getId())
                        .username(post.getUser().getUsername())
                        .fullName(post.getUser().getFullName())
                        .profilePicture(post.getUser().getProfilePicture())
                        .build())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .media(mediaDtos)
                .build();
    }



    @Transactional
    public PostResponseDto createPost(String content, List<MultipartFile> mediaFiles) throws IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (CustomUserDetails) auth.getPrincipal();

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            List<PostMedia> mediaList = mediaFiles.stream()
                    .map(file -> {
                        try {
                            var uploadResult = cloudinaryService.uploadFile(file, "minisocial");
                            String url = (String) uploadResult.get("secure_url");
                            String publicId = (String) uploadResult.get("public_id");
                            String mediaType = file.getContentType().startsWith("image/") ? "image" : "video";
                            PostMedia media = new PostMedia(url, mediaType, publicId);
                            media.setPost(post);
                            return media;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload file to Cloudinary", e);
                        }
                    })
                    .collect(Collectors.toList());

            post.setMedia(mediaList);
        }

        return toDto(postRepository.save(post));
    }



    @Transactional
    public Post updatePost(Integer postId, String newContent, List<MultipartFile> newMediaFiles) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setContent(newContent);

        // Xóa media cũ (trên Cloudinary + DB)
        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            for (PostMedia oldMedia : post.getMedia()) {
                cloudinaryService.deleteFile(oldMedia.getPublicId());
                postMediaRepository.delete(oldMedia);
            }
            post.getMedia().clear();
        }

        // Upload media mới nếu có
        if (newMediaFiles != null && !newMediaFiles.isEmpty()) {
            List<PostMedia> mediaList = newMediaFiles.stream()
                    .map(file -> {
                        try {
                            var uploadResult = cloudinaryService.uploadFile(file, "minisocial");
                            String url = (String) uploadResult.get("secure_url");
                            String publicId = (String) uploadResult.get("public_id");
                            String mediaType = file.getContentType().startsWith("image/") ? "image" : "video";
                            PostMedia media = new PostMedia(url, mediaType, publicId);
                            media.setPost(post);
                            return media;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload file to Cloudinary", e);
                        }
                    })
                    .collect(Collectors.toList());

            post.setMedia(mediaList);
        }

        return postRepository.save(post);
    }

//    public List<Post> getPostByUserId(Integer userId) {
//       return postRepository.findByUserIdWithMedia(userId);
//    }
}