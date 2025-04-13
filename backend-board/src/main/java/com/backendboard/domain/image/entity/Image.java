package com.backendboard.domain.image.entity;

import com.backendboard.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String originalFileName;

	@Column(nullable = false)
	private String storedFileName;

	@Column(nullable = false)
	private String imageType;

	@Column(nullable = false)
	private Long fileSize;

	private Long imageCollectionId;

	@Builder
	private Image(String originalFileName, String storedFileName, String imageType, Long fileSize) {
		this.originalFileName = originalFileName;
		this.storedFileName = storedFileName;
		this.imageType = imageType;
		this.fileSize = fileSize;
		this.imageCollectionId = null;
	}

	public void delete() {
		imageCollectionId = null;
	}
}
