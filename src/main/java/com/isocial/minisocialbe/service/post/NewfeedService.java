package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.dto.post.MediaResponseDto;
import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.enums.TargetType;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.repository.FollowRepository;
import com.isocial.minisocialbe.repository.LikeRepository;
import com.isocial.minisocialbe.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewfeedService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getNewsfeed(Long currentUserId, Pageable pageable) {
        // 1. Lấy following IDs
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(currentUserId);
        followingIds.add(currentUserId);

        // 2. Query posts
        Page<Post> posts = postRepository.findByUserIdIn(followingIds, pageable);

        // 3. Lấy danh sách postIds từ kết quả
        List<Long> postIds = posts.getContent().stream()
                .map(Post::getId)
                .toList();

        // 4. Query một lần: những post nào user đã like?
        Set<Long> likedPostIds = likeRepository.findLikedTargetIds(
                currentUserId,
                TargetType.POST,
                postIds
        );

        // 5. Map sang DTO với isLiked
        return posts.map(post -> toDto(post, likedPostIds.contains(post.getId())));
    }

    private PostResponseDto toDto(Post post, Boolean isLiked) {
        List<MediaResponseDto> mediaDtos = post.getMedia() != null
                ? post.getMedia().stream()
                .map(m -> new MediaResponseDto(m.getId(), m.getMediaUrl(), m.getMediaType()))
                .toList()
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
                .isLiked(isLiked)  // <-- set giá trị
                .build();
    }
}
