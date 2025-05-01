package com.backendboard.domain.post.respository;

import java.util.Map;

public interface ViewCountRedisRepository {
	Long getCount(String postId);

	void save(String postId, Long viewCount);

	void delete();

	Map<Object, Object> getEntries();
}
