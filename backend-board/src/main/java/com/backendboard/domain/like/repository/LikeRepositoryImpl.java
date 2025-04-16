package com.backendboard.domain.like.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
	private static final String KEY_PREFIX = "refresh:";

	private final RedisTemplate<String, Object> redisTemplate;
}
