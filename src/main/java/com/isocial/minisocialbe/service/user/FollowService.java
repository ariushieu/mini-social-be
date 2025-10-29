package com.isocial.minisocialbe.service.user;

import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.model.Follow;
import com.isocial.minisocialbe.model.FollowId;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.FollowRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final FollowService followServiceProxy;

    private final EntityManager entityManager;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, @Lazy FollowService followServiceProxy, EntityManager entityManager) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.followServiceProxy = followServiceProxy;
        this.entityManager = entityManager;
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }
        if (followRepository.existsByFollower_IdAndFollowing_Id(followerId, followingId)) {
            throw new IllegalArgumentException("You already follow this user.");
        }

        Follow follow = Follow.builder()
                .id(FollowId.builder().follower(followerId).following(followingId).build())
                .follower(userRepository.getReferenceById(followerId)) // Gây Dirty Checking
                .following(userRepository.getReferenceById(followingId)) // Gây Dirty Checking
                .createdAt(LocalDateTime.now())
                .build();
        followRepository.save(follow);

        followServiceProxy.updateFollowCounts(followerId, followingId, true);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollower_IdAndFollowing_Id(followerId, followingId)
                .orElseThrow(() -> new ResourceNotFoundException("You do not follow this user."));

        followRepository.delete(follow);

        followServiceProxy.updateFollowCounts(followerId, followingId, false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFollowCounts(Long followerId, Long followingId, boolean isFollow) {
        entityManager.clear();

        if (isFollow) {
            userRepository.incrementFollowingCount(followerId);
            userRepository.incrementFollowerCount(followingId);
        } else {
            userRepository.decrementFollowingCount(followerId);
            userRepository.decrementFollowerCount(followingId);
        }
    }

    public List<Long> getFollowingIds(Long userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }
}

