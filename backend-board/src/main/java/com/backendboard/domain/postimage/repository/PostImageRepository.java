package com.backendboard.domain.postimage.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.postimage.entity.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

	Slice<PostImage> findByPostId(Long postId, Pageable pageable);

	List<PostImage> findByPostId(Long postId);
}
