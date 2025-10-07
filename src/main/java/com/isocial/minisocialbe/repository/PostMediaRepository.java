package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  PostMediaRepository extends JpaRepository<PostMedia,Integer> {
    List<PostMedia> findByPostId(Integer postId);
}
