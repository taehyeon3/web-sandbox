package com.backendboard.domain.postlike.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class PostLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long postId;

	@Builder
	private PostLike(Long userId, Long postId) {
		this.userId = userId;
		this.postId = postId;
	}

	public static PostLike create(Long userId, Long postId) {
		return PostLike.builder()
			.userId(userId)
			.postId(postId)
			.build();
	}
}
