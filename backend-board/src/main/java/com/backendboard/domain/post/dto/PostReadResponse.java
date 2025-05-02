package com.backendboard.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.backendboard.domain.post.entity.Post;
import com.backendboard.domain.postimage.dto.PostImageReadResponse;
import com.backendboard.domain.postimage.entity.PostImage;

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

	@Schema(description = "이미지 아이디", example = "[1, 2, 3]")
	private final List<PostImageReadResponse> images;

	@Schema(description = "생성 시간")
	private final LocalDateTime createdDate;

	@Schema(description = "최근 수정 시간")
	private final LocalDateTime lastModifiedDate;

	@Builder
	private PostReadResponse(Long id, String author, String title, String content, Long likeCount, Long viewCount,
		List<PostImageReadResponse> images, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.content = content;
		this.likeCount = likeCount;
		this.viewCount = viewCount;
		this.images = images;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	@Builder
	public static PostReadResponse toDto(Post post, String author, List<PostImage> images) {
		return PostReadResponse.builder()
			.id(post.getId())
			.author(author)
			.title(post.getTitle())
			.content(post.getContent())
			.likeCount(post.getLikeCount())
			.viewCount(post.getViewCount())
			.images(images.stream().map(PostImageReadResponse::toDto).toList())
			.createdDate(post.getCreatedDate())
			.lastModifiedDate(post.getLastModifiedDate())
			.build();
	}
}
