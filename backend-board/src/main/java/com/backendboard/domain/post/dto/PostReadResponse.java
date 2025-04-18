package com.backendboard.domain.post.dto;

import com.backendboard.domain.post.entity.Post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "게시글 보기 응답 DTO")
@Getter
public class PostReadResponse {
	@Schema(description = "게시글 아이디", example = "1")
	private final Long id;

	@Schema(description = "게시글 작성자", example = "홍길동")
	private final String author;

	@Schema(description = "게시글 제목", example = "제목입니다.")
	private final String title;

	@Schema(description = "게시글 내용", example = "내용입니다.")
	private final String content;

	@Schema(description = "좋아요 수", example = "0")
	private final Long likeCount;

	@Schema(description = "조회수", example = "0")
	private final Long viewCount;

	@Builder
	private PostReadResponse(Long id, String author, String title, String content, Long likeCount, Long viewCount) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.content = content;
		this.likeCount = likeCount;
		this.viewCount = viewCount;
	}

	@Builder

	public static PostReadResponse toDto(Post post, String author) {
		return PostReadResponse.builder()
			.id(post.getId())
			.author(author)
			.title(post.getTitle())
			.content(post.getContent())
			.likeCount(post.getLikeCount())
			.viewCount(post.getViewCount())
			.build();
	}
}
