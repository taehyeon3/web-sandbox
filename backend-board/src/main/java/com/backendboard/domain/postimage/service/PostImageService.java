package com.backendboard.domain.postimage.service;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.postimage.dto.PostImageCreateResponse;
import com.backendboard.domain.postimage.dto.PostImageReadResponse;
import com.backendboard.domain.postimage.dto.PostImageSliceResponse;
import com.backendboard.domain.postimage.dto.PostImageUpdateResponse;

public interface PostImageService {
	PostImageCreateResponse uploadImage(MultipartFile multipartFile) throws IOException;

	PostImageReadResponse getImage(Long imageId);

	PostImageUpdateResponse updateImage(Long imageId, MultipartFile multipartFile) throws IOException;

	void deleteImage(Long imageId);

	Slice<PostImageSliceResponse> getImagesSlice(Long postId, Pageable pageable);
}
