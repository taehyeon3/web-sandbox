package com.backendboard.domain.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;
import com.backendboard.domain.post.entity.Post;
import com.backendboard.domain.post.respository.PostRepository;
import com.backendboard.domain.postimage.repository.PostImageRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public PostCreateResponse createPost(PostCreateRequest request, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Post post = PostCreateRequest.toEntity(request, user.getNickname());
		postRepository.save(post);
		return PostCreateResponse.toDto(post);
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
