package com.backendboard.domain.comment.dto;

import java.time.LocalDateTime;

import com.backendboard.domain.comment.entity.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "댓글 슬라이스 응답 DTO")
@Getter
public class CommentSliceResponse {
	@Schema(description = "댓글 아이디", example = "1")
	private final Long id;

	@Schema(description = "게시판 아이디", example = "1")
	private final Long postId;

	@Schema(description = "게시판 작성자", example = "홍길동")
	private final String author;

	@Schema(description = "댓글 내용", example = "안녕하세요.")
	private final String content;

	@Schema(description = "생성 시간")
	private final LocalDateTime createdDate;

	@Schema(description = "최근 수정 시간")
	private final LocalDateTime lastModifiedDate;

	@Builder
	private CommentSliceResponse(Long id, Long postId, String author, String content, LocalDateTime createdDate,
		LocalDateTime lastModifiedDate) {
		this.id = id;
		this.postId = postId;
		this.author = author;
		this.content = content;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	public static CommentSliceResponse toDto(Comment comment, String author) {
		return CommentSliceResponse.builder()
			.id(comment.getId())
			.postId(comment.getPostId())
			.content(comment.getContent())
			.createdDate(comment.getCreatedDate())
			.lastModifiedDate(comment.getLastModifiedDate())
			.author(author)
			.build();
	}
}
