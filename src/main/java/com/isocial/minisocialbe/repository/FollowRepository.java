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

    // 1. Kiểm tra tồn tại (Đã đúng)
    boolean existsById_FollowerAndId_Following(Long followerId, Long followingId);

    // 2. Tìm kiếm (Giữ lại phiên bản cú pháp đúng)
    Optional<Follow> findById_FollowerAndId_Following(Long followerId, Long followingId);

    // 3. Đếm số lượng Followers (Ai theo dõi người này)
    long countById_Following(Long followingId);

    // 4. Đếm số lượng Following (Người này theo dõi ai)
    long countById_Follower(Long followerId);

    // 5. Lấy danh sách Following (Đã đúng)
    List<Follow> findAllById_Follower(Long followerId);

    // 6. Lấy danh sách Followers (Đã đúng)
    List<Follow> findAllById_Following(Long followingId);

    // 7. Native Query (Vẫn đúng)
    @Query(value = "SELECT following_id FROM follows WHERE follower_id = :followerId", nativeQuery = true)
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.id.follower = :followerId AND f.id.following = :followingId")
    void deleteByFollowerAndFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}