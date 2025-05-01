package com.backendboard.domain.postlike.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.postlike.dto.PostLikeCountResponse;
import com.backendboard.domain.postlike.dto.PostLikeStatusResponse;
import com.backendboard.domain.postlike.entity.PostLike;
import com.backendboard.domain.postlike.repository.PostLikeRedisRepository;
import com.backendboard.domain.postlike.repository.PostLikeRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
	private final PostLikeRedisRepository postLikeRedisRepository;
	private final PostLikeRepository postLikeRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public PostLikeStatusResponse toggleLike(Long authUserId, Long postId) {
		User user = userRepository.getByAuthUserId(authUserId);
		boolean isLiked = postLikeRepository.deleteByUserIdAndPostId(user.getId(), postId) == 0;
		String postIdKey = postId.toString();
		long delta = -1L;

		if (isLiked) {
			PostLike postLike = PostLike.create(user.getId(), postId);
			postLikeRepository.save(postLike);
			delta = 1L;
		}
		postLikeRedisRepository.incrementCount(postIdKey, delta);
		return PostLikeStatusResponse.toDto(isLiked);
	}

	@Override
	public PostLikeStatusResponse getLikeStatus(Long authUserId, Long postId) {
		User user = userRepository.getByAuthUserId(authUserId);
		boolean isLiked = postLikeRepository.existsByUserIdAndPostId(user.getId(), postId);
		return PostLikeStatusResponse.toDto(isLiked);
	}

	@Override
	public PostLikeCountResponse getLikeCount(Long postId) {
		String postIdKey = postId.toString();
		Long count = postLikeRedisRepository.getCount(postIdKey);

		if (count == null) {
			count = postLikeRepository.countByPostId(postId);
			postLikeRedisRepository.save(postIdKey, count);
		}
		return PostLikeCountResponse.toDto(count);
	}
}
