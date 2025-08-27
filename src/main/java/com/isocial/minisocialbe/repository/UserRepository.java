package com.isocial.minisocialbe.repository;

import com.isocial.minisocialbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
