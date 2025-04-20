package com.backendboard.domain.postimage.service;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.postimage.dto.PostImageCreateResponse;
import com.backendboard.domain.postimage.dto.PostImageReadResponse;
import com.backendboard.domain.postimage.dto.PostImageSliceResponse;
import com.backendboard.domain.postimage.dto.PostImageUpdateResponse;
import com.backendboard.domain.postimage.entity.PostImage;
import com.backendboard.domain.postimage.repository.PostImageRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;
import com.backendboard.global.util.FileUtil;
import com.backendboard.global.util.dto.FileInfo;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {
	private final PostImageRepository postImageRepository;
	private final FileUtil fileUtil;

	@Transactional
	@Override
	public PostImageCreateResponse uploadImage(MultipartFile multipartFile) throws IOException {
		String fileType = fileUtil.getFileType(multipartFile.getOriginalFilename());
		validateImageType(fileType);

		FileInfo fileInfo = fileUtil.saveFile(multipartFile);
		PostImage postImage = FileInfo.toEntity(fileInfo);
		postImageRepository.save(postImage);
		return PostImageCreateResponse.toDto(postImage);
	}

	@Override
	public PostImageReadResponse getImage(Long imageId) {
		PostImage postImage = postImageRepository.findById(imageId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_NOT_FOUND));
		return PostImageReadResponse.toDto(postImage);
	}

	@Transactional
	@Override
	public PostImageUpdateResponse updateImage(Long imageId, MultipartFile multipartFile) throws IOException {
		String fileType = fileUtil.getFileType(multipartFile.getOriginalFilename());
		validateImageType(fileType);

		PostImage postImage = postImageRepository.findById(imageId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_NOT_FOUND));
		fileUtil.deleteFile(postImage.getStoredFileName());
		FileInfo fileInfo = fileUtil.saveFile(multipartFile);
		postImage.update(fileInfo.getOriginalFileName(), fileInfo.getStoredFileName(), fileInfo.getContentType(),
			fileInfo.getFileSize());
		return PostImageUpdateResponse.toDto(postImage);
	}

	@Transactional
	@Override
	public void deleteImage(Long imageId) {
		PostImage postImage = postImageRepository.findById(imageId)
			.orElseThrow(() -> new CustomException(CustomError.IMAGE_NOT_FOUND));
		fileUtil.deleteFile(postImage.getStoredFileName());
		postImageRepository.delete(postImage);
	}

	@Override
	public Slice<PostImageSliceResponse> getImagesSlice(Long postId, Pageable pageable) {
		return postImageRepository.findByPostId(postId, pageable).map(PostImageSliceResponse::toDto);
	}

	private void validateImageType(String fileType) {
		if (!fileUtil.isValidateImageType(fileType)) {
			throw new CustomException(CustomError.IMAGE_INVALID_TYPE);
		}
	}
}
