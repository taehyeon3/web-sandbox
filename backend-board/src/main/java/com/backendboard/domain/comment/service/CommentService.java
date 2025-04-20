package com.backendboard.domain.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.dto.CommentReadResponse;
import com.backendboard.domain.comment.dto.CommentSliceResponse;
import com.backendboard.domain.comment.dto.CommentUpdateRequest;
import com.backendboard.domain.comment.dto.CommentUpdateResponse;

public interface CommentService {
	CommentCreateResponse createComment(CommentCreateRequest request, Long authUserId);

	CommentUpdateResponse updateComment(CommentUpdateRequest request, Long commentId, Long authUserId);

	CommentReadResponse getComment(Long commentId);

	void deleteComment(Long commentId, Long authUserId);

	Slice<CommentSliceResponse> getCommetsSlice(Long postId, Pageable pageable);
}
