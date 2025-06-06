package com.backendboard.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.auth.entity.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
	boolean existsByUsername(String username);

	AuthUser findObjectByUsername(String username);

	Optional<AuthUser> findByUsername(String username);
}
