package com.backendboard.domain.postimage.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.postimage.dto.PostImageCreateResponse;
import com.backendboard.domain.postimage.dto.PostImageReadResponse;
import com.backendboard.domain.postimage.dto.PostImageUpdateResponse;

public interface PostImageService {
	PostImageCreateResponse uploadImage(MultipartFile multipartFile) throws IOException;

	PostImageReadResponse getImage(Long imageId);

	PostImageUpdateResponse updateImage(Long imageId, MultipartFile multipartFile) throws IOException;

	void deleteImage(Long imageId);
}
