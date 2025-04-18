package com.backendboard.domain.post.service;

import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;

public interface PostService {
	PostCreateResponse createPost(PostCreateRequest request, Long authUserId);

	PostUpdateResponse updatePost(PostUpdateRequest request, Long postId, Long authUserId);

	PostReadResponse getPost(Long postId);

	void deletePost(Long postId, Long authUserId);
}
