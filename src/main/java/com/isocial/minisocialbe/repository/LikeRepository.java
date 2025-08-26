package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
}
