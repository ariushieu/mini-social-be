package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.enums.TargetType;
import com.isocial.minisocialbe.model.Like;
import com.isocial.minisocialbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);

    Optional<Like> findByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);

    long countByTargetIdAndTargetType(Long targetId, TargetType targetType);

    void deleteByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);

    @Query("SELECT l.user FROM Like l WHERE l.targetId = :targetId AND l.targetType = :targetType")
    List<User> findUsersByTargetIdAndTargetType(@Param("targetId") Long targetId, @Param("targetType") TargetType targetType);

    // Thêm vào LikeRepository.java
    @Query("SELECT l.targetId FROM Like l WHERE l.user.id = :userId " +
            "AND l.targetType = :targetType AND l.targetId IN :targetIds")
    Set<Long> findLikedTargetIds(@Param("userId") Long userId,
                                 @Param("targetType") TargetType targetType,
                                 @Param("targetIds") List<Long> targetIds);

}
