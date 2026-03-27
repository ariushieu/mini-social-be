package com.isocial.minisocialbe.service.post;


import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.enums.TargetType;
import com.isocial.minisocialbe.mapper.PostMapper;
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

@Service
@RequiredArgsConstructor
public class TrendingService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getTrendingPosts(Long currentUserId, Pageable pageable) {
        // 1. Lấy danh sách users đã follow + chính mình (để loại trừ)
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(currentUserId);
        followingIds.add(currentUserId);

        // 2. Random posts từ những người CHƯA follow
        Page<Post> posts = postRepository.findRandomPostsPageable(followingIds, pageable);

        // 3. Batch check isLiked
        List<Long> postIds = posts.getContent().stream()
                .map(Post::getId)
                .toList();

        Set<Long> likedPostIds = likeRepository.findLikedTargetIds(
                currentUserId, TargetType.POST, postIds);

        // 4. Map to DTO
        return posts.map(post -> postMapper.toDto(post, likedPostIds.contains(post.getId())
        ));
    }

}
