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
	private String author;

	@Column(nullable = false, columnDefinition = "VARCHAR(100)")
	private String title;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private Long likeCount;

	@Column(nullable = false)
	private Long viewCount;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	private Post(String author, String title, String content) {
		this.author = author;
		this.title = title;
		this.content = content;
		this.isDeleted = false;
		this.likeCount = 0L;
		this.viewCount = 0L;
	}
}
