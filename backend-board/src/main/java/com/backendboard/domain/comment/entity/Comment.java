package com.backendboard.domain.comment.entity;

import com.backendboard.global.entity.BaseEntity;

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
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long postId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, columnDefinition = "VARCHAR(1000)")
	private String content;

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	private Comment(Long postId, Long userId, String content) {
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.deleted = false;
	}

	public static Comment createComment(Long postId, Long userId, String content) {
		return Comment.builder()
			.postId(postId)
			.userId(userId)
			.content(content)
			.build();
	}

	public void update(String content) {
		this.content = content;
	}

	public void delete() {
		this.deleted = true;
	}
}
