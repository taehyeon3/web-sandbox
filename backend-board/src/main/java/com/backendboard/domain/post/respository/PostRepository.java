package com.backendboard.domain.post.respository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backendboard.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Slice<Post> findBy(Pageable pageable);
}
