package com.backendboard.domain.image.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.image.dto.ImageCreateResponse;
import com.backendboard.domain.image.dto.ImageUpdateResponse;
import com.backendboard.domain.image.entity.Image;
import com.backendboard.domain.image.repository.ImageRepository;
import com.backendboard.global.util.FileUtil;
import com.backendboard.global.util.dto.FileInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	private final ImageRepository imageRepository;
	private final FileUtil fileUtil;

	@Override
	public ImageCreateResponse uploadImage(MultipartFile multipartFile) throws IOException {
		FileInfo fileInfo = fileUtil.saveFile(multipartFile);
		Image image = FileInfo.toEntity(fileInfo);
		imageRepository.save(image);
		return ImageCreateResponse.toDto(image);
	}

	@Override
	public ImageUpdateResponse updateImage(Long imageId, MultipartFile multipartFile) {
		return null;
	}

	@Override
	public void deleteImage(Long imageId) {

	}
}
