package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.enums.TargetType;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.model.Like;
import com.isocial.minisocialbe.repository.LikeRepository;
import com.isocial.minisocialbe.repository.PostRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class PostLikeServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LikeRepository likeRepository;
    @InjectMocks
    private PostLikeService postLikeService;

    @Test
    void likePost_postNotFound_throwsResourceNotFoundException() {
        Long userId = 1L;
        Long postId = 2L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThatThrownBy(() -> postLikeService.likePost(userId, postId))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(postRepository, never()).incrementLikeCount(postId);
    }

    @Test
    void likePost_alreadyLiked_throwsIllegalStateException() {
        Long userId = 1L;
        Long postId = 2L;

        when(postRepository.existsById(postId)).thenReturn(true);
        when(likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST)).thenReturn(true);

        assertThatThrownBy(() -> postLikeService.likePost(userId, postId))
                .isInstanceOf(IllegalStateException.class);
        verify(postRepository, never()).incrementLikeCount(postId);
    }

    @Test
    void likePost_validInput_savesLikeAndIncrementsCount() {
        Long userId = 1L;
        Long postId = 2L;
        when(postRepository.existsById(postId)).thenReturn(true);
        when(likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST)).thenReturn(false);

        postLikeService.likePost(userId, postId);

        verify(likeRepository).save(any());
        verify(postRepository).incrementLikeCount(postId);
    }

    @Test
    void unlikePost_notLiked_throwsResourceNotFoundException(){
        Long userId = 1L;
        Long postId = 2L;

        when(likeRepository.findByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST)).thenReturn(Optional.empty());
        assertThatThrownBy(()->postLikeService.unlikePost(userId, postId))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(likeRepository, never()).delete(any());
        verify(postRepository, never()).decrementLikeCount(postId);
    }

    @Test
    void unlikePost_validInput_deletesLikeAndDecrementCount(){
        Long userId = 1L;
        Long postId = 2L;
        Like like = new Like();

        when(likeRepository.findByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST)).thenReturn(Optional.of(like));
        postLikeService.unlikePost(userId, postId);
        verify(likeRepository).delete(any());
        verify(postRepository).decrementLikeCount(postId);
    }
}
