package com.backendboard.domain.image.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.image.dto.ImageCollectionCreateRequest;
import com.backendboard.domain.image.dto.ImageCollectionCreateResponse;
import com.backendboard.domain.image.dto.ImageCollectionReadResponse;
import com.backendboard.domain.image.dto.ImageCollectionUpdateRequest;
import com.backendboard.domain.image.dto.ImageCollectionUpdateResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ImageCollectionServiceImpl implements ImageCollectionService {

	@Override
	public ImageCollectionCreateResponse createImageCollection(ImageCollectionCreateRequest request) {
		return null;
	}

	@Override
	public ImageCollectionReadResponse getImageCollection(Long imageCollectionId) {
		return null;
	}

	@Override
	public ImageCollectionUpdateResponse updateImageCollection(ImageCollectionUpdateRequest request,
		Long imageCollectionId) {
		return null;
	}

	@Override
	public void deleteImageCollection(Long imageCollectionId) {

	}
}
