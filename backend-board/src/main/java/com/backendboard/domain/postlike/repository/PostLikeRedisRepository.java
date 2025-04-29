package com.backendboard.domain.postlike.repository;

public interface PostLikeRedisRepository {
	Long getCount(String postId);

	void save(String postId, Long likeCount);

	void delete();
}
