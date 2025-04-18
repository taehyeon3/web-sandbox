package com.backendboard.domain.postimage;

import org.springframework.stereotype.Component;

import com.backendboard.domain.postimage.entity.PostImage;
import com.backendboard.domain.postimage.repository.PostImageRepository;
import com.backendboard.global.util.dto.FileInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageMockData {
	private final PostImageRepository postImageRepository;

	public PostImage createImage(FileInfo fileInfo) {
		PostImage postImage = FileInfo.toEntity(fileInfo);
		return postImageRepository.save(postImage);
	}
}
