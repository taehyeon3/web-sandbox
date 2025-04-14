package com.backendboard.domain.image;

import org.springframework.stereotype.Component;

import com.backendboard.domain.image.entity.Image;
import com.backendboard.domain.image.repository.ImageRepository;
import com.backendboard.global.util.dto.FileInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageMockData {
	private final ImageRepository imageRepository;

	public Image createImage(FileInfo fileInfo) {
		Image image = FileInfo.toEntity(fileInfo);
		return imageRepository.save(image);
	}
}
