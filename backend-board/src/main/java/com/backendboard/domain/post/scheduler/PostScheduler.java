package com.backendboard.domain.post.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.backendboard.domain.postlike.repository.PostLikeRedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostScheduler {
	private static final String UPDATE_LIKE_COUNT_SQL = "UPDATE post SET like_count = ? WHERE id = ?";

	private final PostLikeRedisRepository postLikeRedisRepository;
	private final JdbcTemplate jdbcTemplate;

	@Scheduled(fixedDelay = 60_000)
	public void syncLikeCount() {
		Map<Object, Object> entries = postLikeRedisRepository.getEntries();
		List<Object[]> batchArgs = new ArrayList<>();

		for (Map.Entry<Object, Object> entry : entries.entrySet()) {
			String postId = (String)entry.getKey();
			long likeCount = ((Number)entry.getValue()).longValue();
			batchArgs.add(new Object[] {likeCount, Long.valueOf(postId)});
		}

		jdbcTemplate.batchUpdate(UPDATE_LIKE_COUNT_SQL, batchArgs);
		postLikeRedisRepository.delete();
	}
}
