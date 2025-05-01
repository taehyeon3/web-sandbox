package com.backendboard.domain.post.respository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ViewCountRedisRepositoryImpl implements ViewCountRedisRepository {
	private static final String KEY = "post:view:count";

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Long getCount(String postId) {
		return Optional.ofNullable(redisTemplate.opsForHash().get(KEY, postId))
			.map(value -> ((Number)value).longValue())
			.orElse(null);
	}

	@Override
	public void save(String postId, Long viewCount) {
		redisTemplate.opsForHash().put(KEY, postId, viewCount);
	}

	@Override
	public void delete() {
		redisTemplate.delete(KEY);
	}

	@Override
	public Map<Object, Object> getEntries() {
		return redisTemplate.opsForHash().entries(KEY);
	}
}
