package com.backendboard.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, columnDefinition = "VARCHAR(200)")
	private String title;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private Long likeCount;

	@Column(nullable = false)
	private Long viewCount;

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	private Post(Long userId, String title, String content) {
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.deleted = false;
		this.likeCount = 0L;
		this.viewCount = 0L;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void delete() {
		this.deleted = true;
	}
}
