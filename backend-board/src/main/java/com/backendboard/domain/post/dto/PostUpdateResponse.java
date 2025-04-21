package com.backendboard.domain.post.dto;

import java.util.List;

import com.backendboard.domain.post.entity.Post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "게시글 수정 응답 DTO")
@Getter
public class PostUpdateResponse {
	@Schema(description = "게시글 아이디", example = "1")
	private final Long id;

	@Schema(description = "게시글 작성자", example = "홍길동")
	private final String author;

	@Schema(description = "게시글 제목", example = "제목입니다.")
	private final String title;

	@Schema(description = "게시글 내용", example = "내용입니다.")
	private final String content;

	@Schema(description = "이미지 아이디", example = "[1, 2, 3]")
	private final List<Long> imageIds;

	@Builder
	private PostUpdateResponse(Long id, String author, String title, String content, List<Long> imageIds) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.content = content;
		this.imageIds = imageIds;
	}

	public static PostUpdateResponse toDto(Post post, String author, List<Long> imageIds) {
		return PostUpdateResponse.builder()
			.id(post.getId())
			.author(author)
			.title(post.getTitle())
			.content(post.getContent())
			.imageIds(imageIds)
			.build();
	}
}
