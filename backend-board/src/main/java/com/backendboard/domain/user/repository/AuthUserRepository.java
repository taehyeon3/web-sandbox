package com.backendboard.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.user.entitiy.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
	boolean existsByUsername(String username);
}
