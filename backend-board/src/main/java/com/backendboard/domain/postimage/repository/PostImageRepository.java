package com.backendboard.domain.postimage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.postimage.entity.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
	List<PostImage> findByIdInAndImageCollectionIdIsNull(List<Long> ids);

	List<PostImage> findByImageCollectionId(Long imageCollectionId);
}
