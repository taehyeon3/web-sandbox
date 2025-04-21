package com.backendboard.domain.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.post.PostMockData;
import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.entity.Post;
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

	@Autowired
	private TestDataUtil testDataUtil;

	@Autowired
	private PostMockData postMockData;

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
			PostCreateRequest request = PostCreateRequest.builder()
				.title("제목")
				.content("내용")
				.imageIds(new ArrayList<>())
				.build();
			Post post = postMockData.createPost(user);

			// when & then
			mockMvc.perform(post("/posts")
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value(post.getTitle()))
				.andExpect(jsonPath("$.content").value(post.getContent()))
				.andExpect(jsonPath("$.author").value(user.getNickname()));
		}
	}

	@Nested
	@DisplayName("게시글 수정 API")
	class UpdatePost {

		@Test
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			PostUpdateRequest request = PostUpdateRequest.builder()
				.title("제목 변경")
				.content("내용 변경")
				.imageIds(new ArrayList<>())
				.build();
			Post post = postMockData.createPost(user);

			// when & then
			mockMvc.perform(put("/posts/{postId}", post.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(post.getId()))
				.andExpect(jsonPath("$.title").value(post.getTitle()))
				.andExpect(jsonPath("$.content").value(post.getContent()));
		}

		@Test
		@DisplayName("게시글을 찾을 수 없습니다. 404")
		void notFoundPost() throws Exception {
			// given
			PostUpdateRequest request = PostUpdateRequest.builder()
				.title("제목 변경")
				.content("내용 변경")
				.imageIds(new ArrayList<>())
				.build();
			Post post = postMockData.createPost(user);

			// when & then
			mockMvc.perform(put("/posts/{postId}", post.getId() + 1)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("게시글 조회 API")
	class ReadPost {

		@Test
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			Post post = postMockData.createPost(user);

			// when & then
			mockMvc.perform(get("/posts/{postId}", post.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(post.getTitle()))
				.andExpect(jsonPath("$.content").value(post.getContent()))
				.andExpect(jsonPath("$.author").value(user.getNickname()));
		}

		@Test
		@DisplayName("게시글을 찾을 수 없습니다. 404")
		void notFoundPost() throws Exception {
			// given
			Post post = postMockData.createPost(user);

			// when & then
			mockMvc.perform(get("/posts/{postId}", post.getId() + 1))
				.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("게시글 삭제 API")
	class DeletePost {

		@Test
		@DisplayName("성공 204")
		void success() throws Exception {
			Post post = postMockData.createPost(user);

			mockMvc.perform(delete("/posts/{postId}", post.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNoContent());
		}

		@Test
		@DisplayName("게시글을 찾을 수 없습니다. 404")
		void notFoundPost() throws Exception {
			// given
			Post post = postMockData.createPost(user);

			// when & then
			mockMvc.perform(delete("/posts/{postId}", post.getId() + 1)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNotFound());
		}
	}
}
