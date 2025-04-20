package com.backendboard.domain.post.service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostSliceResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;
import com.backendboard.domain.post.entity.Post;
import com.backendboard.domain.post.respository.PostRepository;
import com.backendboard.domain.postimage.repository.PostImageRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

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
		Post post = PostCreateRequest.toEntity(request, user.getId());
		postRepository.save(post);
		return PostCreateResponse.toDto(post, user.getNickname());
	}

	@Transactional
	@Override
	public PostUpdateResponse updatePost(PostUpdateRequest request, Long postId, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomError.POST_NOT_FOUND));

		validateAuthor(post, user);

		post.update(request.getTitle(), request.getContent());
		return PostUpdateResponse.toDto(post, user.getNickname());
	}

	@Override
	public PostReadResponse getPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomError.POST_NOT_FOUND));
		User user = userRepository.findById(post.getUserId())
			.orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));
		return PostReadResponse.toDto(post, user.getNickname());
	}

	@Transactional
	@Override
	public void deletePost(Long postId, Long authUserId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomError.POST_NOT_FOUND));
		User user = userRepository.getByAuthUserId(authUserId);

		validateAuthor(post, user);

		post.delete();
	}

	@Override
	public Slice<PostSliceResponse> getPostsSlice(Pageable pageable) {
		Slice<Post> posts = postRepository.findBy(pageable);

		Set<Long> userIds = posts.stream().map(Post::getUserId).collect(Collectors.toSet());

		Map<Long, User> userMap = userRepository.findAllById(userIds)
			.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		return posts.map(post -> {
			User user = userMap.get(post.getUserId());
			return PostSliceResponse.toDto(post, user.getNickname());
		});
	}

	public void validateAuthor(Post post, User user) {
		if (post.getUserId() != user.getId()) {
			throw new CustomException(CustomError.POST_NOT_AUTHOR);
		}
	}
}
