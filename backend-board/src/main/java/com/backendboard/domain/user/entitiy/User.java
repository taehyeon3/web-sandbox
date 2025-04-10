package com.backendboard.domain.user.entitiy;

import com.backendboard.domain.auth.entitiy.AuthUser;
import com.backendboard.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String nickname;

	@OneToOne
	@JoinColumn(name = "auth_user_id")
	private AuthUser authUser;

	@Builder
	private User(String username, String nickname, AuthUser authUser) {
		this.username = username;
		this.nickname = nickname;
		this.authUser = authUser;
	}
}
