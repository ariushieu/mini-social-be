package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByContent(String content);
//    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.media WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
//    List<Post> findByUserIdWithMedia(@Param("userId") Integer userId);
}
