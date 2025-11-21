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
    @Query(value = "UPDATE users SET follower_count = follower_count + 1 WHERE id = :userId", nativeQuery = true)
    void incrementFollowerCount(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE users SET follower_count = follower_count - 1 WHERE id = :userId", nativeQuery = true)
    void decrementFollowerCount(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE users SET following_count = following_count + 1 WHERE id = :userId", nativeQuery = true)
    void incrementFollowingCount(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE users SET following_count = following_count - 1 WHERE id = :userId", nativeQuery = true)
    void decrementFollowingCount(@Param("userId") Long userId);
}
