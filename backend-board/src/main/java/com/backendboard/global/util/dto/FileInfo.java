package com.backendboard.global.util.dto;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.postimage.entity.PostImage;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FileInfo {
	private final String originalFileName;
	private final String storedFileName;
	private final String contentType;
	private final Long fileSize;

	@Builder
	private FileInfo(String originalFileName, String storedFileName, String contentType, Long fileSize) {
		this.originalFileName = originalFileName;
		this.storedFileName = storedFileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
	}

	public static FileInfo toDto(MultipartFile multipartFile, File file) {
		return FileInfo.builder()
			.originalFileName(multipartFile.getOriginalFilename())
			.storedFileName(file.getName())
			.contentType(multipartFile.getContentType())
			.fileSize(multipartFile.getSize())
			.build();
	}

	public static PostImage toEntity(FileInfo info) {
		return PostImage.builder()
			.originalFileName(info.originalFileName)
			.storedFileName(info.storedFileName)
			.imageType(info.contentType)
			.fileSize(info.fileSize)
			.build();
	}
}
