package com.isocial.minisocialbe.service.user;

import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.dto.user.ProfileResponseDto;
import com.isocial.minisocialbe.dto.post.MediaResponseDto;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.repository.PostRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ProfileResponseDto getProfileByUserId(Integer userId) {
        ProfileResponseDto profile = userRepository.findProfileByUserId(userId);
        if (profile == null) {
            throw new RuntimeException("User profile not found for userId: " + userId);
        }
        List<PostResponseDto> posts = postRepository.findByUserId(userId).stream()
                .map(this::toPostResponseDto)
                .collect(Collectors.toList());
        profile.setPosts(posts);
        return profile;
    }

    private PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
//                .user(new AuthorResponseDto(post.getUser()))
                .user(AuthorResponseDto.builder()
                        .id(post.getUser().getId())
                        .username(post.getUser().getUsername())
                        .fullName(post.getUser().getFullName())
                        .profilePicture(post.getUser().getProfilePicture())
                        .build())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .media(post.getMedia() != null
                        ? post.getMedia().stream()
                                .map(m -> new MediaResponseDto(m.getId(), m.getMediaUrl(), m.getMediaType()))
                                .collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
