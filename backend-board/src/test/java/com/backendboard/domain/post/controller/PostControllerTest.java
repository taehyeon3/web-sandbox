package com.backendboard.domain.post.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;
import com.backendboard.domain.post.service.PostService;
import com.backendboard.domain.user.entity.User;
import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.util.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WithMockUser
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	PostService postService;

	@Autowired
	private TestDataUtil testDataUtil;

	private CustomUserDetails userDetails;

	private User user;

	@BeforeEach
	void setUp() {
		AuthUser authUser = testDataUtil.createAuthUser("user", "1234", UserRole.USER);

		userDetails = new CustomUserDetails(authUser);

		user = testDataUtil.createUser(authUser, "홍길동", "감자");
	}

	@Nested
	@DisplayName("게시글 생성 API")
	class CreatePost {

		@Test
		@DisplayName("성공 201")
		void success() throws Exception {
			// given
			PostCreateRequest request = new PostCreateRequest("제목", "내용");
			PostCreateResponse response = new PostCreateResponse(1L, "제목", "내용", "testuser");

			BDDMockito.given(postService.createPost(any(PostCreateRequest.class), eq(1L)))
				.willReturn(response);

			// when & then
			mockMvc.perform(post("/posts")
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.title").value("제목"))
				.andExpect(jsonPath("$.content").value("내용"))
				.andExpect(jsonPath("$.author").value("testuser"));
		}
	}

	@Nested
	@DisplayName("게시글 수정 API")
	class UpdatePost {

		@Test
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			PostUpdateRequest request = new PostUpdateRequest("수정제목", "수정내용");
			PostUpdateResponse response = new PostUpdateResponse(1L, "수정제목", "수정내용");

			BDDMockito.given(postService.updatePost(any(PostUpdateRequest.class), eq(1L), eq(1L)))
				.willReturn(response);

			// when & then
			mockMvc.perform(put("/posts/{postId}", 1L)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.title").value("수정제목"))
				.andExpect(jsonPath("$.content").value("수정내용"));
		}
	}

	@Nested
	@DisplayName("게시글 조회 API")
	class ReadPost {

		@Test
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			PostReadResponse response = new PostReadResponse(1L, "제목", "내용", "testuser");

			BDDMockito.given(postService.getPost(1L)).willReturn(response);

			// when & then
			mockMvc.perform(get("/posts/{postId}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.title").value("제목"))
				.andExpect(jsonPath("$.content").value("내용"))
				.andExpect(jsonPath("$.author").value("testuser"));
		}
	}

	@Nested
	@DisplayName("게시글 삭제 API")
	class DeletePost {

		@Test
		@DisplayName("성공 204")
		void success() throws Exception {
			BDDMockito.willDoNothing().given(postService).deletePost(1L, 1L);

			mockMvc.perform(delete("/posts/{postId}", 1L)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNoContent());
		}
	}
}
