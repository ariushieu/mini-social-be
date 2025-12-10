package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    List<Comment> findByParentCommentId(Long parentCommentId);

    long countByPostId(Long postId);
    // Tăng like count
    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    // Giảm like count
    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId")
    void decrementLikeCount(@Param("commentId") Long commentId);

    // Tăng reply count
    @Modifying
    @Query("UPDATE Comment c SET c.replyCount = c.replyCount + 1 WHERE c.id = :commentId")
    void incrementReplyCount(@Param("commentId") Long commentId);

    // Giảm reply count
    @Modifying
    @Query("UPDATE Comment c SET c.replyCount = c.replyCount - 1 WHERE c.id = :commentId")
    void decrementReplyCount(@Param("commentId") Long commentId);
}
