package com.backendboard.domain.like.service;

import com.backendboard.domain.like.dto.LikeResponse;

public interface LikeService {
	LikeResponse togglePostLike(Long authUserId, Long postId);
}
