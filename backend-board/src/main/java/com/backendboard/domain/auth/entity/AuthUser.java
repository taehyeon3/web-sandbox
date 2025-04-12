package com.backendboard.domain.auth.entity;

import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.user.entity.User;
import com.backendboard.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "VARCHAR(100)")
	private UserRole role;

	private boolean status;

	@Builder
	private AuthUser(String username, String password, UserRole role) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = true;
	}

	public static AuthUser createAuthUser(String username, String password, UserRole role) {
		return AuthUser.builder()
			.username(username)
			.password(password)
			.role(role)
			.build();
	}

	public User createUser(String username, String nickname) {
		return User.builder()
			.username(username)
			.nickname(nickname)
			.authUser(this)
			.build();
	}

	public void deactivate() {
		this.status = false;
	}
}
