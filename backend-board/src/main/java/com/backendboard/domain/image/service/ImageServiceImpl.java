package com.backendboard.domain.image.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.image.dto.ImageCreateResponse;
import com.backendboard.domain.image.dto.ImageReadResponse;
import com.backendboard.domain.image.dto.ImageUpdateResponse;
import com.backendboard.domain.image.entity.Image;
import com.backendboard.domain.image.repository.ImageRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;
import com.backendboard.global.util.FileUtil;
import com.backendboard.global.util.dto.FileInfo;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	private final ImageRepository imageRepository;
	private final FileUtil fileUtil;

	@Transactional
	@Override
	public ImageCreateResponse uploadImage(MultipartFile multipartFile) throws IOException {
		String fileType = fileUtil.getFileType(multipartFile.getOriginalFilename());
		validateImageType(fileType);

		FileInfo fileInfo = fileUtil.saveFile(multipartFile);
		Image image = FileInfo.toEntity(fileInfo);
		imageRepository.save(image);
		return ImageCreateResponse.toDto(image);
	}

	@Override
	public ImageReadResponse getImage(Long imageId) {
		return null;
	}

	@Transactional
	@Override
	public ImageUpdateResponse updateImage(Long imageId, MultipartFile multipartFile) throws IOException {
		String fileType = fileUtil.getFileType(multipartFile.getOriginalFilename());
		validateImageType(fileType);

		Image image = imageRepository.findById(imageId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_NOT_FOUND));
		fileUtil.deleteFile(image.getStoredFileName());
		FileInfo fileInfo = fileUtil.saveFile(multipartFile);
		image.updateFile(fileInfo);
		return ImageUpdateResponse.toDto(image);
	}

	@Transactional
	@Override
	public void deleteImage(Long imageId) {
		Image image = imageRepository.findById(imageId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_NOT_FOUND));
		fileUtil.deleteFile(image.getStoredFileName());
		imageRepository.delete(image);
	}

	private void validateImageType(String fileType) {
		if (!fileUtil.isValidateImageType(fileType)) {
			throw new CustomException(CustomError.IMAGE_INVALID_TYPE);
		}
	}
}
