package com.backendboard.domain.image.service;

import com.backendboard.domain.image.dto.ImageCollectionCreateRequest;
import com.backendboard.domain.image.dto.ImageCollectionCreateResponse;
import com.backendboard.domain.image.dto.ImageCollectionReadResponse;
import com.backendboard.domain.image.dto.ImageCollectionUpdateRequest;
import com.backendboard.domain.image.dto.ImageCollectionUpdateResponse;

public interface ImageCollectionService {
	ImageCollectionCreateResponse createImageCollection(ImageCollectionCreateRequest request);

	ImageCollectionReadResponse getImageCollection(Long imageCollectionId);

	ImageCollectionUpdateResponse updateImageCollection(ImageCollectionUpdateRequest request, Long imageCollectionId);

	void deleteImageCollection(Long imageCollectionId);
}
