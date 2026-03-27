package com.isocial.minisocialbe.mapper;

import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.dto.post.MediaResponseDto;
import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {
    public PostResponseDto toDto(Post post) {
        List<MediaResponseDto> mediaDtos = post.getMedia() != null
                ? post.getMedia().stream()
                .map(m -> new MediaResponseDto(m.getId(), m.getMediaUrl(), m.getMediaType()))
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

    public PostResponseDto toDto(Post post, Boolean isLiked) {
        PostResponseDto dto = toDto(post);
        dto.setIsLiked(isLiked);
        return dto;
    }
}
