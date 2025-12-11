package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.post.AuthorResponseDto;
import com.isocial.minisocialbe.enums.TargetType;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.model.Like;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.CommentRepository;
import com.isocial.minisocialbe.repository.LikeRepository;
import com.isocial.minisocialbe.repository.PostRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository  commentRepository;

    @Transactional
    public void likePost(Long userId, Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }

        if (likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST)) {
            throw new IllegalStateException("You are already liked this post");
        }

        Like like = Like.builder()
                .user(userRepository.getReferenceById(userId))
                .targetId(postId)
                .targetType(TargetType.POST)
                .createdAt(LocalDateTime.now())
                .build();
        likeRepository.save(like);

        postRepository.incrementLikeCount(postId);
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST)
                .orElseThrow(() -> new ResourceNotFoundException("You haven't liked this post"));

        likeRepository.delete(like);
        postRepository.decrementLikeCount(postId);
    }

    public boolean isPostLikedByUser(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, postId, TargetType.POST);
    }

    public List<AuthorResponseDto> getUsersWhoLikedPost(Long postId) {
        List<User> users = likeRepository.findUsersByTargetIdAndTargetType(postId, TargetType.POST);
        return users.stream()
                .map(u -> new AuthorResponseDto(u.getId(), u.getUsername(), u.getFullName(), u.getProfilePicture()))
                .toList();
    }

    @Transactional
    public void likeComment(Long userId, Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found");
        }

        if (likeRepository.existsByUserIdAndTargetIdAndTargetType(
                userId, commentId, TargetType.COMMENT)) {
            throw new IllegalStateException("You already liked this comment");
        }

        Like like = Like.builder()
                .user(userRepository.getReferenceById(userId))
                .targetId(commentId)
                .targetType(TargetType.COMMENT)
                .createdAt(LocalDateTime.now())
                .build();
        likeRepository.save(like);

        commentRepository.incrementLikeCount(commentId);
    }

    /**
     * Unlike một comment
     */
    @Transactional
    public void unlikeComment(Long userId, Long commentId) {
        Like like = likeRepository.findByUserIdAndTargetIdAndTargetType(
                        userId, commentId, TargetType.COMMENT)
                .orElseThrow(() -> new ResourceNotFoundException("You haven't liked this comment"));

        likeRepository.delete(like);
        commentRepository.decrementLikeCount(commentId);
    }

    /**
     * Kiểm tra user đã like comment chưa
     */
    public boolean isCommentLikedByUser(Long userId, Long commentId) {
        return likeRepository.existsByUserIdAndTargetIdAndTargetType(
                userId, commentId, TargetType.COMMENT);
    }

    /**
     * Lấy danh sách users đã like comment
     */
    public List<AuthorResponseDto> getUsersWhoLikedComment(Long commentId) {
        List<User> users = likeRepository.findUsersByTargetIdAndTargetType(
                commentId, TargetType.COMMENT);
        return users.stream()
                .map(u -> new AuthorResponseDto(u.getId(), u.getUsername(),
                        u.getFullName(), u.getProfilePicture()))
                .toList();
    }
}
