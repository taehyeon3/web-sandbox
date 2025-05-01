package com.backendboard.domain.post.respository;

import java.util.Map;

public interface ViewCountRedisRepository {
	void delete();

	Map<Object, Object> getEntries();

	void incrementCount(String postId);

	Long getIncrementCount(String postId);
}
