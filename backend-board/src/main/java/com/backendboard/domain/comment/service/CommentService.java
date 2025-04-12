package com.backendboard.domain.comment.service;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.dto.CommentReadResponse;
import com.backendboard.domain.comment.dto.CommentUpdateRequest;
import com.backendboard.domain.comment.dto.CommentUpdateResponse;

public interface CommentService {
	CommentCreateResponse createComment(CommentCreateRequest request, Long authUserId);

	CommentUpdateResponse updateComment(CommentUpdateRequest request, Long commentId, Long authUserId);

	CommentReadResponse getComment(Long commentId, Long authUserId);
}
