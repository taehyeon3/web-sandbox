package com.backendboard.domain.image.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.image.dto.ImageCollectionCreateRequest;
import com.backendboard.domain.image.dto.ImageCollectionCreateResponse;
import com.backendboard.domain.image.dto.ImageCollectionReadResponse;
import com.backendboard.domain.image.dto.ImageCollectionUpdateRequest;
import com.backendboard.domain.image.dto.ImageCollectionUpdateResponse;
import com.backendboard.domain.image.entity.Image;
import com.backendboard.domain.image.entity.ImageCollection;
import com.backendboard.domain.image.repository.ImageCollectionRepository;
import com.backendboard.domain.image.repository.ImageRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ImageCollectionServiceImpl implements ImageCollectionService {
	private final ImageCollectionRepository imageCollectionRepository;
	private final ImageRepository imageRepository;

	@Transactional
	@Override
	public ImageCollectionCreateResponse createImageCollection(ImageCollectionCreateRequest request) {
		List<Long> imageIds = request.getImageIds();
		List<Image> images = imageRepository.findByIdInAndImageCollectionIdIsNull(imageIds);

		validateImageIds(imageIds, images);

		ImageCollection imageCollection = ImageCollectionCreateRequest.toEntity();
		imageCollectionRepository.save(imageCollection);
		for (Image image : images) {
			image.updateCollectionId(imageCollection.getId());
		}
		return ImageCollectionCreateResponse.toDto(imageCollection.getId(), images);
	}

	@Override
	public ImageCollectionReadResponse getImageCollection(Long imageCollectionId) {
		ImageCollection imageCollection = imageCollectionRepository.findById(imageCollectionId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_COLLECTION_NOT_FOUND));
		List<Image> images = imageRepository.findByImageCollectionId(imageCollectionId);
		return ImageCollectionReadResponse.toDto(imageCollection.getId(), images);
	}

	@Transactional
	@Override
	public ImageCollectionUpdateResponse updateImageCollection(ImageCollectionUpdateRequest request,
		Long imageCollectionId) {
		List<Long> imageIds = request.getImageIds();
		ImageCollection imageCollection = imageCollectionRepository.findById(imageCollectionId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_COLLECTION_NOT_FOUND));
		List<Image> images = imageRepository.findAllById(imageIds);

		validateImageIds(imageIds, images);

		for (Image image : images) {
			image.updateCollectionId(imageCollection.getId());
		}
		return ImageCollectionUpdateResponse.toDto(imageCollection.getId(), images);
	}

	@Transactional
	@Override
	public void deleteImageCollection(Long imageCollectionId) {

	}

	private static void validateImageIds(List<Long> imageIds, List<Image> images) {
		if (imageIds.size() != images.size()) {
			throw new CustomException(CustomError.IMAGE_NOT_FOUND);
		}
	}
}
