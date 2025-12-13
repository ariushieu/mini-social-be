package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByContent(String content);

    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.user u LEFT JOIN FETCH p.media m WHERE u.id = :userId")
    List<Post> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE posts SET like_count = like_count + 1 WHERE id = :postId", nativeQuery = true)
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query(value = "UPDATE posts SET like_count = like_count - 1 WHERE id = :postId", nativeQuery = true)
    void decrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + 1 WHERE p.id = :postId")
    void incrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount - 1 WHERE p.id = :postId")
    void decrementCommentCount(@Param("postId") Long postId);

    // Lấy posts từ danh sách user IDs (có phân trang)
    @Query("SELECT p FROM Post p JOIN FETCH p.user u LEFT JOIN FETCH p.media " +
            "WHERE u.id IN :userIds ORDER BY p.createdAt DESC")
    Page<Post> findByUserIdIn(@Param("userIds") List<Long> userIds, Pageable pageable);

    // Hoặc dùng native query nếu cần tối ưu performance
    @Query(value = "SELECT * FROM posts WHERE user_id IN :userIds ORDER BY created_at DESC",
            countQuery = "SELECT COUNT(*) FROM posts WHERE user_id IN :userIds",
            nativeQuery = true)
    Page<Post> findByUserIdInNative(@Param("userIds") List<Long> userIds, Pageable pageable);

}
