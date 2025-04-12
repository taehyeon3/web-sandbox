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
	private String author;

	@Column(nullable = false, columnDefinition = "VARCHAR(500)")
	private String content;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	private Comment(Long postId, String author, String content) {
		this.postId = postId;
		this.author = author;
		this.content = content;
		this.isDeleted = false;
	}

	public static Comment createComment(Long postId, String author, String content) {
		return Comment.builder()
			.postId(postId)
			.author(author)
			.content(content)
			.build();
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void delete() {
		this.isDeleted = true;
	}
}
