package com.backendboard.domain.postlike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.postlike.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	int deleteByUserIdAndPostId(Long userId, Long postId);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	Long countByPostId(Long postId);
}
