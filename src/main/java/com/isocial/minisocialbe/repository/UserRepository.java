package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.dto.user.ProfileResponseDto;
import com.isocial.minisocialbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByVerificationCode(String verificationCode);

    @Query("SELECT new com.isocial.minisocialbe.dto.user.ProfileResponseDto(u.id, u.username, u.fullName, u.bio, u.profilePicture, u.followerCount, u.followingCount, u.joinDate, u.lastLogin) " +
            "FROM User u WHERE u.id = :userId")
    ProfileResponseDto findProfileByUserId(Integer userId);
}
