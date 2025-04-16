package com.backendboard.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.image.entity.ImageCollection;

@Repository
public interface ImageCollectionRepository extends JpaRepository<ImageCollection, Long> {
}
