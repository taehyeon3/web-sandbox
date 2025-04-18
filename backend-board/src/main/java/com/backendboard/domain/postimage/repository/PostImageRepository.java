package com.backendboard.domain.postimage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.postimage.entity.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
