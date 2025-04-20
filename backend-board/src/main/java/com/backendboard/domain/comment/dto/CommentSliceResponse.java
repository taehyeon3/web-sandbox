package com.backendboard.domain.comment.dto;

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

	@Builder
	private CommentSliceResponse(Long id, Long postId, String author, String content) {
		this.id = id;
		this.postId = postId;
		this.author = author;
		this.content = content;
	}

	public static CommentSliceResponse toDto(Comment comment, String author) {
		return CommentSliceResponse.builder()
			.id(comment.getId())
			.postId(comment.getPostId())
			.content(comment.getContent())
			.author(author)
			.build();
	}
}
