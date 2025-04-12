package com.backendboard.domain.comment.service;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;

public interface CommentService {
	CommentCreateResponse createComment(CommentCreateRequest request, Long authUserId);
}
