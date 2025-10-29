package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.dto.user.ProfileResponseDto;
import com.isocial.minisocialbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByVerificationCode(String verificationCode);

    @Query("SELECT new com.isocial.minisocialbe.dto.user.ProfileResponseDto(u.id, u.username, u.fullName, u.bio, u.profilePicture, u.followerCount, u.followingCount, u.joinDate, u.lastLogin) " +
            "FROM User u WHERE u.id = :userId")
    ProfileResponseDto findProfileByUserId(@Param("userId") Long userId);

    Optional<User> findUserByRefreshToken(String token);

    @Modifying
    @Query("UPDATE User u SET u.followerCount = u.followerCount + 1 WHERE u.id = :userId")
    void incrementFollowerCount(@Param("userId") Long userId);

    // Giảm số lượng người theo dõi (Follower Count)
    @Modifying
    @Query("UPDATE User u SET u.followerCount = u.followerCount - 1 WHERE u.id = :userId")
    void decrementFollowerCount(@Param("userId") Long userId);

    // Tăng số lượng người mà User đang theo dõi (Following Count)
    @Modifying
    @Query("UPDATE User u SET u.followingCount = u.followingCount + 1 WHERE u.id = :userId")
    void incrementFollowingCount(@Param("userId") Long userId);

    // Giảm số lượng người mà User đang theo dõi (Following Count)
    @Modifying
    @Query("UPDATE User u SET u.followingCount = u.followingCount - 1 WHERE u.id = :userId")
    void decrementFollowingCount(@Param("userId") Long userId);
}
