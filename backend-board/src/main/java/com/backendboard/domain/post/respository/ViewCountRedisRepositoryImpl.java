package com.backendboard.domain.post.respository;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ViewCountRedisRepositoryImpl implements ViewCountRedisRepository {
	private static final String KEY = "post:view:count";

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void delete() {
		redisTemplate.delete(KEY);
	}

	@Override
	public Map<Object, Object> getEntries() {
		return redisTemplate.opsForHash().entries(KEY);
	}

	@Override
	public void incrementCount(String postId) {
		redisTemplate.opsForHash().increment(KEY, postId, 1);
	}
}
