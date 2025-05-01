package com.backendboard.domain.postlike.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostLikeRedisRepositoryImpl implements PostLikeRedisRepository {
	private static final String KEY = "post:like";

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Long getCount(String postId) {
		return Optional.ofNullable(redisTemplate.opsForHash().get(KEY, postId))
			.map(value -> ((Number)value).longValue())
			.orElse(null);
	}

	@Override
	public void save(String postId, Long likeCount) {
		redisTemplate.opsForHash().put(KEY, postId, likeCount);
	}

	@Override
	public void delete() {
		redisTemplate.delete(KEY);
	}

	@Override
	public Map<Object, Object> getEntries() {
		return redisTemplate.opsForHash().entries(KEY);
	}

	@Override
	public void incrementCount(String postId, Long delta) {
		redisTemplate.opsForHash().increment(KEY, postId, delta);
	}
}
