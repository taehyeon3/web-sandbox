package com.backendboard.domain.post.service;

import org.springframework.stereotype.Service;

import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	@Override
	public PostCreateResponse createPost(PostCreateRequest request, Long authUserId) {
		return null;
	}

	@Override
	public PostUpdateResponse updatePost(PostUpdateRequest request, Long postId, Long authUserId) {
		return null;
	}

	@Override
	public PostReadResponse getPost(Long postId) {
		return null;
	}

	@Override
	public void deletePost(Long postId, Long authUserId) {

	}
}
