package com.backendboard.domain.post.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostSliceResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;

public interface PostService {
	PostCreateResponse createPost(PostCreateRequest request, Long authUserId);

	PostUpdateResponse updatePost(PostUpdateRequest request, Long postId, Long authUserId);

	PostReadResponse getPost(Long postId, Long currentAuthUserId);

	void deletePost(Long postId, Long authUserId);

	Slice<PostSliceResponse> getPostsSlice(Pageable pageable);
}
