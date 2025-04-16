package com.backendboard.domain.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.like.dto.LikeResponse;
import com.backendboard.domain.like.entity.PostLike;
import com.backendboard.domain.like.repository.PostLikeRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
	private final PostLikeRepository postLikeRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public LikeResponse togglePostLike(Long authUserId, Long postId) {
		User user = userRepository.getByAuthUserId(authUserId);
		boolean isLiked = postLikeRepository.deleteByUserIdAndPostId(user.getId(), postId) == 0;

		if (isLiked) {
			PostLike postLike = PostLike.create(user.getId(), postId);
			postLikeRepository.save(postLike);
		}
		return LikeResponse.toDto(isLiked);
	}
}
