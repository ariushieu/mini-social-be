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
public class NewfeedService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper;

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

        // 5.
        return posts.map(post -> postMapper.toDto(post, likedPostIds.contains(post.getId())
        ));
    }


}
