package com.backendboard.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.auth.entity.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
	boolean existsByUsername(String username);

	AuthUser findByUsername(String username);
}
