package com.backendboard.domain.postlike.service;

import com.backendboard.domain.postlike.dto.PostLikeCountResponse;
import com.backendboard.domain.postlike.dto.PostLikeStatusResponse;

public interface PostLikeService {
	PostLikeStatusResponse toggleLike(Long authUserId, Long postId);

	PostLikeStatusResponse getLikeStatus(Long authUserId, Long postId);

	PostLikeCountResponse getLikeCount(Long authUserId, Long postId);
}
