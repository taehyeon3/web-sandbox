package com.backendboard.domain.user.entitiy;

import com.backendboard.domain.user.entitiy.type.UserRole;
import com.backendboard.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
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

	public AuthUser(String username, String password, UserRole role) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = true;
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
