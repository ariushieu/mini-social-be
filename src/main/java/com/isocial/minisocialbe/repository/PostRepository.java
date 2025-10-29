package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.dto.post.PostResponseDto;
import com.isocial.minisocialbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByContent(String content);

    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.user u LEFT JOIN FETCH p.media m WHERE u.id = :userId")
    List<Post> findByUserId(@Param("userId") Long userId);
}
