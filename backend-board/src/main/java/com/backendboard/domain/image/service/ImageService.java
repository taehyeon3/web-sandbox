package com.backendboard.domain.image.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.image.dto.ImageCreateResponse;
import com.backendboard.domain.image.dto.ImageReadResponse;
import com.backendboard.domain.image.dto.ImageUpdateResponse;

public interface ImageService {
	ImageCreateResponse uploadImage(MultipartFile multipartFile) throws IOException;

	ImageReadResponse getImage(Long imageId);

	ImageUpdateResponse updateImage(Long imageId, MultipartFile multipartFile) throws IOException;

	void deleteImage(Long imageId);
}
