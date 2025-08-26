package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.Follow;
import com.isocial.minisocialbe.model.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    List<Follow> findByIdFollower(Integer followerId);
    List<Follow> findByIdFollowing(Integer followingId);
}
