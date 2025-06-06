package com.backendboard.domain.postimage.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.postimage.ImageMockData;
import com.backendboard.domain.postimage.entity.PostImage;
import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.global.util.FileUtil;
import com.backendboard.global.util.dto.FileInfo;
import com.backendboard.util.TestDataUtil;
import com.jayway.jsonpath.JsonPath;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PostPostImageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CustomUserDetails userDetails;

	@Autowired
	private TestDataUtil testDataUtil;

	@Autowired
	private FileUtil fileUtil;

	@Autowired
	private ImageMockData imageMockData;

	MockMultipartFile imageFile;

	FileInfo fileInfo;

	@BeforeEach
	void setUp() throws IOException {
		AuthUser authUser = testDataUtil.createAuthUser("user", "1234", UserRole.USER);
		userDetails = new CustomUserDetails(authUser);
		testDataUtil.createUser(authUser, "홍길동", "감자");
		imageFile = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "이미지 테스트 데이터".getBytes());
	}

	@AfterEach
	void cleanUp() {
		fileUtil.deleteFile(fileInfo.getStoredFileName());
	}

	@Nested
	@DisplayName("이미지 생성 테스트")
	class CreatePostImage {

		@Test
		@WithMockUser
		@DisplayName("성공 201")
		void success() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);
			fileUtil.deleteFile(postImage.getStoredFileName());

			// when & then
			MvcResult result = mockMvc.perform(multipart("/post-images")
					.file(imageFile)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.fileName").value("test.jpg"))
				.andReturn();
			String responseContent = result.getResponse().getContentAsString();
			String fileUrl = JsonPath.read(responseContent, "$.fileUrl");
			fileUtil.deleteFile(fileUrl);
		}
	}

	@Nested
	@DisplayName("이미지 조회 테스트")
	class ReadPostImage {

		@Test
		@WithMockUser
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);

			// when & then
			mockMvc.perform(get("/post-images/{imageId}", postImage.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(postImage.getId()))
				.andExpect(jsonPath("$.fileName").value(postImage.getOriginalFileName()))
				.andExpect(jsonPath("$.fileUrl").value(postImage.getStoredFileName()));
		}

		@Test
		@WithMockUser
		@DisplayName("이미지를 찾을 수 없습니다. 404")
		void notFoundImage() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);

			// when & then
			mockMvc.perform(get("/post-images/{imageId}", postImage.getId() + 1)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("이미지 수정 테스트")
	class UpdatePostImage {

		@Test
		@WithMockUser
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);

			// when & then
			mockMvc.perform(multipart("/post-images/{imageId}", postImage.getId())
					.file(imageFile)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.with(request -> {
						request.setMethod("PUT");
						return request;
					}))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(postImage.getId()))
				.andExpect(jsonPath("$.fileName").value(postImage.getOriginalFileName()))
				.andExpect(jsonPath("$.fileUrl").value(postImage.getStoredFileName()));
			fileUtil.deleteFile(postImage.getStoredFileName());
		}

		@Test
		@WithMockUser
		@DisplayName("이미지를 찾을 수 없습니다. 404")
		void notFoundImage() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);

			// when & then
			mockMvc.perform(multipart("/post-images/{imageId}", postImage.getId() + 1)
					.file(imageFile)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.with(request -> {
						request.setMethod("PUT");
						return request;
					}))
				.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("이미지 삭제 테스트")
	class DeletePostImage {

		@Test
		@WithMockUser
		@DisplayName("성공 204")
		void success() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);

			// when & then
			mockMvc.perform(delete("/post-images/{imageId}", postImage.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNoContent());
		}

		@Test
		@WithMockUser
		@DisplayName("이미지를 찾을 수 없습니다. 404")
		void notFoundImage() throws Exception {
			// given
			fileInfo = fileUtil.saveFile(imageFile);
			PostImage postImage = imageMockData.createImage(fileInfo);

			// when & then
			mockMvc.perform(delete("/post-images/{imageId}", postImage.getId() + 1)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNotFound());
		}
	}
}
