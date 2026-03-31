package com.isocial.minisocialbe.service.user;

import com.isocial.minisocialbe.model.Follow;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.FollowRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;


import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private FollowService followService;

    @Test
    void followUser_sameId_throwsIllegalArgumentException() {
        Long userId = 1L;
        assertThatThrownBy(() -> followService.followUser(userId, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void followUer_validInput_savesFollow() {
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.getReferenceById(followerId)).thenReturn(new User());
        when(userRepository.getReferenceById(followingId)).thenReturn(new User());

        followService.followUser(followerId, followingId);

        verify(followRepository).saveAndFlush(any());
        verify(userRepository).incrementFollowingCount(followerId);
        verify(userRepository).incrementFollowerCount(followingId);
    }

    @Test
    void followUser_alreadyFollowing_throwsIllegalArgumentException() {
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.findById(any())).thenReturn(Optional.of(new Follow()));
        assertThatThrownBy(() -> followService.followUser(followerId, followingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("You already follow this user.");

        verify(followRepository, never()).saveAndFlush(any());
    }

    @Test
    void unFollowUser_exits_throwsResourceNotFoundException(){
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.existsById_FollowerAndId_Following(followerId,followingId)).thenReturn(false);

        assertThatThrownBy(()-> followService.unfollowUser(followerId, followingId))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(followRepository, never()).deleteByFollowerAndFollowing(any(), any());
    }

    @Test
    void unfollowUser_validInput_deletesFollowAndUpdatesCounts(){
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.existsById_FollowerAndId_Following(followerId,followingId)).thenReturn(true);

        followService.unfollowUser(followerId, followingId);

        verify(followRepository).deleteByFollowerAndFollowing(followerId, followingId);
        verify(userRepository).decrementFollowingCount(followerId);
        verify(userRepository).decrementFollowerCount(followingId);

    }
}
