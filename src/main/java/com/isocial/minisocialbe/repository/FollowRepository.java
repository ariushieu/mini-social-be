package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.Follow;
import com.isocial.minisocialbe.model.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    Optional<Follow> findByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    //for new feeds
    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);

    //tong follower
    long countByFollowing_Id(Long followingId);

    //tong following
    long countByFollower_Id(Long followerId);

    List<Follow> findAllByFollower_Id(Long followerId);
    List<Follow> findAllByFollowing_Id(Long  followingId);


}
