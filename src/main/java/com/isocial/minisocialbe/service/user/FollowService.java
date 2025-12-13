package com.isocial.minisocialbe.service.user;

import com.isocial.minisocialbe.dto.user.FollowStatsDto;
import com.isocial.minisocialbe.dto.user.FollowUserDto;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.model.Follow;
import com.isocial.minisocialbe.model.FollowId;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.FollowRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, @Lazy FollowService followServiceProxy, EntityManager entityManager) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        // Dùng findById để check, nếu có thì throw
        if (followRepository.findById(new FollowId(followerId, followingId)).isPresent()) {
            throw new IllegalArgumentException("You already follow this user.");
        }

        User followerRef = userRepository.getReferenceById(followerId);
        User followingRef = userRepository.getReferenceById(followingId);

        Follow follow = Follow.builder()
                .id(FollowId.builder().follower(followerId).following(followingId).build())
                .followerUser(followerRef)
                .followingUser(followingRef)
                .createdAt(LocalDateTime.now())
                .build();

        followRepository.saveAndFlush(follow); // Đổi thành saveAndFlush

        this.updateFollowCounts(followerId, followingId, true);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        if (!followRepository.existsById_FollowerAndId_Following(followerId, followingId)) {
            throw new ResourceNotFoundException("You do not follow this user.");
        }

        followRepository.deleteByFollowerAndFollowing(followerId, followingId);

        this.updateFollowCounts(followerId, followingId, false);
    }

    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFollowCounts(Long followerId, Long followingId, boolean isFollow) {
//        System.out.println("updateFollowCounts called: follower=" + followerId + ", following=" + followingId + ", isFollow=" + isFollow);
//       entityManager.clear();

        if (isFollow) {
            userRepository.incrementFollowingCount(followerId);
            userRepository.incrementFollowerCount(followingId);
        } else {
            userRepository.decrementFollowingCount(followerId);
            userRepository.decrementFollowerCount(followingId);
        }

//        System.out.println("=== END updateFollowCounts ===");
    }

    public List<Long> getFollowingIds(Long userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsById_FollowerAndId_Following(followerId, followingId);
    }

    /**
     * Lấy danh sách followers của một user (ai theo dõi user này)
     */
    @Transactional(readOnly = true)
    public Page<FollowUserDto> getFollowers(Long userId, Long currentUserId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User không tồn tại.");
        }

        Page<Follow> followers = followRepository.findAllById_Following(userId, pageable);
        
        // Lấy danh sách ID của những người mà currentUser đang follow
        Set<Long> currentUserFollowingIds = currentUserId != null 
            ? followRepository.findFollowingIdsByFollowerId(currentUserId).stream().collect(Collectors.toSet())
            : Set.of();

        return followers.map(follow -> mapToFollowUserDto(follow.getFollowerUser(), follow.getCreatedAt(), currentUserFollowingIds));
    }

    /**
     * Lấy danh sách following của một user (user này theo dõi ai)
     */
    @Transactional(readOnly = true)
    public Page<FollowUserDto> getFollowing(Long userId, Long currentUserId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User không tồn tại.");
        }

        Page<Follow> following = followRepository.findAllById_Follower(userId, pageable);
        
        // Lấy danh sách ID của những người mà currentUser đang follow
        Set<Long> currentUserFollowingIds = currentUserId != null 
            ? followRepository.findFollowingIdsByFollowerId(currentUserId).stream().collect(Collectors.toSet())
            : Set.of();

        return following.map(follow -> mapToFollowUserDto(follow.getFollowingUser(), follow.getCreatedAt(), currentUserFollowingIds));
    }

    /**
     * Lấy thống kê follow của một user
     */
    @Transactional(readOnly = true)
    public FollowStatsDto getFollowStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại."));

        return FollowStatsDto.builder()
                .userId(userId)
                .followerCount(user.getFollowerCount())
                .followingCount(user.getFollowingCount())
                .build();
    }

    /**
     * Kiểm tra mutual follow (cả hai đều follow nhau)
     */
    public boolean isMutualFollow(Long userId1, Long userId2) {
        return isFollowing(userId1, userId2) && isFollowing(userId2, userId1);
    }

    private FollowUserDto mapToFollowUserDto(User user, LocalDateTime followedAt, Set<Long> currentUserFollowingIds) {
        return FollowUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .isFollowing(currentUserFollowingIds.contains(user.getId()))
                .followedAt(followedAt)
                .build();
    }
}

