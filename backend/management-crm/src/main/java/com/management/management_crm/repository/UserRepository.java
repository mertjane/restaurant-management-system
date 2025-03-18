package com.management.management_crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.management_crm.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByResetToken(String resetToken);
}
