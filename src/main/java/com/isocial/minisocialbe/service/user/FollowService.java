package com.isocial.minisocialbe.service.user;

import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.model.Follow;
import com.isocial.minisocialbe.model.FollowId;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.FollowRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import jakarta.persistence.EntityManager;
// import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
//@RequiredArgsConstructor: Circular Dependency
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

//    private final FollowService followServiceProxy;

    // private final EntityManager entityManager;


    public FollowService(FollowRepository followRepository, UserRepository userRepository, @Lazy FollowService followServiceProxy, EntityManager entityManager) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
//        this.followServiceProxy = followServiceProxy;
        // this.entityManager = entityManager;
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

        System.out.println("=== END updateFollowCounts ===");
    }

    public List<Long> getFollowingIds(Long userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }
}

