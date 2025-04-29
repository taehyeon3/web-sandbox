package com.backendboard.domain.postlike.repository;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostLikeRedisRepositoryImpl implements PostLikeRedisRepository {
	private static final String KEY = "post:like";

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Long getCount(String postId) {
		return Optional.of(redisTemplate.opsForHash().get(KEY, postId))
			.map(value -> ((Number)value).longValue())
			.orElse(null);
	}

	@Override
	public void save(String postId, Long likeCount) {
		redisTemplate.opsForHash().put(KEY, postId, likeCount);
	}

	@Override
	public void delete() {

	}
}
