package com.isocial.minisocialbe.service.post;

import com.isocial.minisocialbe.dto.comment.CommentCreateDto;
import com.isocial.minisocialbe.dto.comment.CommentResponseDto;
import com.isocial.minisocialbe.dto.comment.CommentUpdateDto;
import com.isocial.minisocialbe.dto.user.UserResponseDto;
import com.isocial.minisocialbe.exception.ResourceNotFoundException;
import com.isocial.minisocialbe.model.Comment;
import com.isocial.minisocialbe.model.Post;
import com.isocial.minisocialbe.model.User;
import com.isocial.minisocialbe.repository.CommentRepository;
import com.isocial.minisocialbe.repository.PostRepository;
import com.isocial.minisocialbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private CommentResponseDto toDto(Comment comment) {
        UserResponseDto userDto = UserResponseDto.builder()
                .id(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .fullName(comment.getUser().getFullName())
                .profilePicture(comment.getUser().getProfilePicture())
                .build();

        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .user(userDto)
                .commentText(comment.getCommentText())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(null) // Không load replies tự động, dùng endpoint riêng
                .build();
    }

    @Transactional
    public CommentResponseDto createComment(CommentCreateDto dto, Long currentUserId) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment.CommentBuilder commentBuilder = Comment.builder()
                .post(post)
                .user(user)
                .commentText(dto.getCommentText())
                .likeCount(0)
                .replyCount(0);
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now());

        if (dto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            commentBuilder.parentComment(parentComment);

            // Tăng reply count của parent comment
            commentRepository.incrementReplyCount(dto.getParentCommentId());
        }

        Comment comment = commentBuilder.build();
        Comment savedComment = commentRepository.save(comment);

        if (dto.getParentCommentId() == null) {
            postRepository.incrementCommentCount(post.getId());
        }
        return toDto(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        if(!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("Post not found");
        }

        List<Comment> comments = commentRepository.findByPostIdAndParentCommentIsNull(postId);
        return comments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getRepliesByCommentId(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found");
        }

        List<Comment> replies = commentRepository.findByParentCommentId(commentId);
        return replies.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto updateComment(CommentUpdateDto dto, Long currentUserId) {
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Kiểm tra quyền sở hữu
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new IllegalStateException("You can only edit your own comments");
        }

        // Sử dụng setter vì không thể rebuild toàn bộ entity đã persist
        comment.setCommentText(dto.getCommentText());

        Comment updatedComment = commentRepository.save(comment);
        return toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Kiểm tra quyền sở hữu
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new IllegalStateException("You can only delete your own comments");
        }

        // Nếu là reply, giảm reply count của parent
        if (comment.getParentComment() != null) {
            commentRepository.decrementReplyCount(comment.getParentComment().getId());
        } else {
            // Nếu là parent comment, giảm comment count của post
            postRepository.decrementCommentCount(comment.getPost().getId());
        }

        commentRepository.delete(comment);
    }
}
