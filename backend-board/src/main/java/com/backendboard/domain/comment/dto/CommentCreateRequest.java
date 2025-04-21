package com.backendboard.domain.comment.dto;

import com.backendboard.domain.comment.entity.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "댓글생성 요청 DTO")
@Getter
public class CommentCreateRequest {
	@Schema(description = "게시판 아이디", example = "1")
	@NotNull
	private final Long postId;

	@Schema(description = "댓글 내용", example = "안녕하세요.")
	@NotBlank
	@Size(max = 1000)
	private final String content;

	@Builder
	private CommentCreateRequest(Long postId, String content) {
		this.postId = postId;
		this.content = content;
	}

	public static Comment toEntity(CommentCreateRequest dto, Long userId) {
		return Comment.builder()
			.postId(dto.postId)
			.content(dto.content)
			.userId(userId)
			.build();
	}
}
