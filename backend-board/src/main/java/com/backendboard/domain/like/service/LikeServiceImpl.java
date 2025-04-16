package com.backendboard.domain.like.service;

import org.springframework.stereotype.Service;

import com.backendboard.domain.like.dto.LikeResponse;
import com.backendboard.domain.like.repository.LikeRepository;
import com.backendboard.domain.like.repository.PostLikeRepository;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
	private static final String KEY_PREFIX = "like:";

	private final LikeRepository likeRepository;
	private final PostLikeRepository postLikeRepository;
	private final UserRepository userRepository;

	@Override
	public LikeResponse togglePostLike(Long authUserId, Long postId) {
		return null;
	}
}
