package com.backendboard.global.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.global.util.dto.FileInfo;

@Component
public class FileUtil {
	private static final Set<String> IMAGE_TYPES = new HashSet<>(
		Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"));
	private final String uploadDirectory;

	private void createDirectory() {
		File directory = new File(uploadDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public FileUtil(@Value("${spring.file.upload.directory}") String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	public String getFileType(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public String generateUniqueFileName(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;
	}

	public FileInfo saveFile(MultipartFile multipartFile) throws IOException {
		createDirectory();
		String uniqueFileName = generateUniqueFileName(multipartFile.getOriginalFilename());
		File file = new File(uploadDirectory, uniqueFileName);
		multipartFile.transferTo(file);
		return FileInfo.toDto(multipartFile, file);
	}

	public void deleteFile(String fileName) {
		File file = new File(uploadDirectory, fileName);
		file.delete();
	}

	public boolean isValidateImageType(String imageType) {
		if (imageType == null || imageType.isEmpty()) {
			return false;
		}
		return IMAGE_TYPES.contains(imageType);
	}
}
