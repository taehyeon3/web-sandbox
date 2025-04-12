package com.backendboard.domain.comment.dto;

import com.backendboard.domain.comment.entity.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "댓글읽기 요청 DTO")
@Getter
public class CommentReadResponse {
	@Schema(description = "댓글 아이디", example = "1")
	private final Long id;

	@Schema(description = "게시판 아이디", example = "1")
	private final Long postId;

	@Schema(description = "게시판 작성자", example = "홍길동")
	private final String author;

	@Schema(description = "댓글 내용", example = "안녕하세요.")
	private final String content;

	@Builder
	private CommentReadResponse(Long id, Long postId, String author, String content) {
		this.id = id;
		this.postId = postId;
		this.author = author;
		this.content = content;
	}

	public static CommentReadResponse toDto(Comment comment, String author) {
		return CommentReadResponse.builder()
			.id(comment.getId())
			.postId(comment.getPostId())
			.content(comment.getContent())
			.author(author)
			.build();
	}
}
