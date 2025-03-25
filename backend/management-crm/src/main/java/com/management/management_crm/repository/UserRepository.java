package com.management.management_crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management_crm.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByResetToken(String resetToken);

    // Custom SQL query to find user by ID
    @Query(value = "SELECT id, email, reset_token, created_at, updated_at FROM users WHERE id = :id", nativeQuery = true)
    Optional<UserEntity> findUserById(@Param("id") Long id);
}
