package com.backendboard.domain.image.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.util.TestDataUtil;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CustomUserDetails userDetails;

	@Autowired
	private TestDataUtil testDataUtil;

	@BeforeEach
	void setUp() {
		AuthUser authUser = testDataUtil.createAuthUser("user", "1234", UserRole.USER);
		userDetails = new CustomUserDetails(authUser);
		testDataUtil.createUser(authUser, "홍길동", "감자");
	}

	@Nested
	@DisplayName("이미지 생성 테스트")
	class CreateImage {

		@Test
		@WithMockUser
		@DisplayName("성공 201")
		void success() throws Exception {
			// given
			MockMultipartFile imageFile = new MockMultipartFile(
				"image",
				"test.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"이미지 테스트 데이터".getBytes()
			);

			// when & then
			mockMvc.perform(multipart("/images")
					.file(imageFile)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.fileName").value("test.jpg"))
				.andExpect(jsonPath("$.imageUrl").value("http://example.com/test.jpg"));
		}
	}

	@Nested
	@DisplayName("이미지 조회 테스트")
	class ReadImage {

		@Test
		@WithMockUser
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			Long imageId = 1L;

			// when & then
			mockMvc.perform(get("/images/{imageId}", imageId)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.fileName").value("test.jpg"))
				.andExpect(jsonPath("$.imageUrl").value("http://example.com/test.jpg"));
		}
	}

	@Nested
	@DisplayName("이미지 수정 테스트")
	class UpdateImage {

		@Test
		@WithMockUser
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			Long imageId = 1L;
			MockMultipartFile imageFile = new MockMultipartFile(
				"image",
				"updated.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"수정된 이미지 테스트 데이터".getBytes()
			);

			// when & then
			mockMvc.perform(multipart("/images/{imageId}", imageId)
					.file(imageFile)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.with(request -> {
						request.setMethod("PUT");
						return request;
					}))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.fileName").value("updated.jpg"))
				.andExpect(jsonPath("$.imageUrl").value("http://example.com/updated.jpg"));
		}
	}

	@Nested
	@DisplayName("이미지 삭제 테스트")
	class DeleteImage {

		@Test
		@WithMockUser
		@DisplayName("성공 204")
		void success() throws Exception {
			// given
			Long imageId = 1L;

			// when & then
			mockMvc.perform(delete("/images/{imageId}", imageId)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNoContent());
		}
	}
}
