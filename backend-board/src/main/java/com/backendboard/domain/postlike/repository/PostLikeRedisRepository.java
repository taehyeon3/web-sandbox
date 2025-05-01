package com.backendboard.domain.postlike.repository;

import java.util.Map;

public interface PostLikeRedisRepository {
	Long getCount(String postId);

	void save(String postId, Long likeCount);

	void delete();

	Map<Object, Object> getEntries();

	void incrementCount(String postId, Long delta);
}
