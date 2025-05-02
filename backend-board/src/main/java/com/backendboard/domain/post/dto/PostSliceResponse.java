package com.backendboard.domain.post.dto;

import java.time.LocalDateTime;

import com.backendboard.domain.post.entity.Post;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "게시글 슬라이스 보기 응답 DTO")
@Getter
public class PostSliceResponse {
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

	@Schema(description = "생성 시간")
	private final LocalDateTime createdDate;

	@Schema(description = "최근 수정 시간")
	private final LocalDateTime lastModifiedDate;

	@Builder
	@QueryProjection
	public PostSliceResponse(Long id, String author, String title, String content, Long likeCount, Long viewCount,
		LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.content = content;
		this.likeCount = likeCount;
		this.viewCount = viewCount;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	@Builder
	public static PostSliceResponse toDto(Post post, String author) {
		return PostSliceResponse.builder()
			.id(post.getId())
			.author(author)
			.title(post.getTitle())
			.content(post.getContent())
			.likeCount(post.getLikeCount())
			.viewCount(post.getViewCount())
			.createdDate(post.getCreatedDate())
			.lastModifiedDate(post.getLastModifiedDate())
			.build();
	}
}
