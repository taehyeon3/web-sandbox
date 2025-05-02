package com.backendboard.domain.post.service;

import java.util.List;

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
import com.backendboard.domain.post.respository.PostQueryRepository;
import com.backendboard.domain.post.respository.PostRepository;
import com.backendboard.domain.post.respository.ViewCountRedisRepository;
import com.backendboard.domain.postimage.entity.PostImage;
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
	private final UserRepository userRepository;
	private final PostImageRepository postImageRepository;
	private final ViewCountRedisRepository viewCountRedisRepository;
	private final PostQueryRepository postQueryRepository;

	@Transactional
	@Override
	public PostCreateResponse createPost(PostCreateRequest request, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Post post = PostCreateRequest.toEntity(request, user.getId());

		postRepository.save(post);

		List<Long> imageIds = request.getImageIds();
		List<PostImage> images = postImageRepository.findAllById(imageIds);

		for (PostImage image : images) {
			image.updatePostId(post.getId());
		}
		return PostCreateResponse.toDto(post, user.getNickname(), imageIds);
	}

	@Transactional
	@Override
	public PostUpdateResponse updatePost(PostUpdateRequest request, Long postId, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomError.POST_NOT_FOUND));

		validateAuthor(post, user);

		post.update(request.getTitle(), request.getContent());

		List<Long> imageIds = request.getImageIds();
		List<PostImage> images = postImageRepository.findAllById(imageIds);

		for (PostImage image : images) {
			image.updatePostId(post.getId());
		}
		return PostUpdateResponse.toDto(post, user.getNickname(), imageIds);
	}

	@Override
	public PostReadResponse getPost(Long postId, Long currentAuthUserId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomError.POST_NOT_FOUND));
		User user = userRepository.findById(post.getUserId())
			.orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

		if (user.getAuthUser().getId() != currentAuthUserId) {
			incrementViewCount(postId);
		}

		List<PostImage> images = postImageRepository.findByPostId(postId);
		return PostReadResponse.toDto(post, user.getNickname(), images);
	}

	private void incrementViewCount(Long postId) {
		String postIdKey = postId.toString();
		viewCountRedisRepository.incrementCount(postIdKey);
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
		return postQueryRepository.findPostSliceResponse(false, pageable);
	}

	public void validateAuthor(Post post, User user) {
		if (post.getUserId() != user.getId()) {
			throw new CustomException(CustomError.POST_NOT_AUTHOR);
		}
	}
}
