package com.backendboard.domain.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.comment.CommentMockData;
import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentUpdateRequest;
import com.backendboard.domain.comment.entity.Comment;
import com.backendboard.domain.user.entity.User;
import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.util.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CustomUserDetails userDetails;

	@MockitoBean
	private CustomUserDetails otherUserDetails;

	@Autowired
	private TestDataUtil testDataUtil;

	@Autowired
	private CommentMockData commentMockData;

	private User user;

	private User otherUser;

	@BeforeEach
	void setUp() {
		AuthUser authUser = testDataUtil.createAuthUser("user", "1234", UserRole.USER);
		AuthUser otherAuthUser = testDataUtil.createAuthUser("otherUser", "1234", UserRole.USER);

		userDetails = new CustomUserDetails(authUser);
		otherUserDetails = new CustomUserDetails(otherAuthUser);

		user = testDataUtil.createUser(authUser, "홍길동", "감자");
		otherUser = testDataUtil.createUser(otherAuthUser, "김철수", "고구마");

	}

	@Nested
	@DisplayName("댓글 생성 테스트")
	class CreateCommentTest {

		@Test
		@WithMockUser
		@DisplayName("성공 201")
		void success() throws Exception {
			// given
			CommentCreateRequest request = CommentCreateRequest.builder().postId(1L).content("테스트 댓글입니다.").build();

			// when & then
			mockMvc.perform(post("/comments")
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.content").value("테스트 댓글입니다."))
				.andExpect(jsonPath("$.postId").value(1L))
				.andDo(print());
		}

		@Test
		@WithMockUser
		@DisplayName("댓글 생성 실패 - 유효하지 않은 요청")
		void createComment_InvalidRequest() throws Exception {
			// given
			CommentCreateRequest request = CommentCreateRequest.builder().postId(1L).content("안녕하세요").build();

			// when & then
			mockMvc.perform(post("/comments")
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(print());
		}
	}

	@Nested
	@DisplayName("댓글 수정 테스트")
	class UpdateCommentTest {

		@Test
		@WithMockUser
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			Comment comment = commentMockData.createComment(user);
			CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정했습니다.").build();

			// when & then
			mockMvc.perform(put("/comments/{commentId}", comment.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(comment.getId()))
				.andExpect(jsonPath("$.content").value(comment.getContent()))
				.andExpect(jsonPath("$.postId").value(comment.getPostId()))
				.andDo(print());
		}

		@Test
		@WithMockUser
		@DisplayName("작성자가 본인이 아님 403")
		void notAuthor() throws Exception {
			// given
			Comment comment = commentMockData.createComment(user);
			CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정했습니다.").build();

			// when & then
			mockMvc.perform(put("/comments/{commentId}", comment.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(otherUserDetails))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isForbidden())
				.andDo(print());
		}
	}

	@Nested
	@DisplayName("댓글 조회 테스트")
	class ReadCommentTest {

		@Test
		@WithMockUser
		@DisplayName("성공 200")
		void success() throws Exception {
			// given
			Comment comment = commentMockData.createComment(user);

			// when & then
			mockMvc.perform(get("/comments/{commentId}", comment.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(comment.getId()))
				.andExpect(jsonPath("$.content").value(comment.getContent()))
				.andExpect(jsonPath("$.postId").value(comment.getPostId()))
				.andDo(print());
		}

		@Test
		@WithMockUser
		@DisplayName("존재하지 않는 댓글 404")
		void notFoundComment() throws Exception {
			// given
			Comment comment = commentMockData.createComment(user);

			// when & then
			mockMvc.perform(get("/comments/{commentId}", comment.getId() + 1)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNotFound())
				.andDo(print());
		}
	}

	@Nested
	@DisplayName("댓글 삭제 테스트")
	class DeleteCommentTest {

		@Test
		@WithMockUser
		@DisplayName("성공 204")
		void success() throws Exception {
			// given
			Comment comment = commentMockData.createComment(user);

			// when & then
			mockMvc.perform(delete("/comments/{commentId}", comment.getId())
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNoContent())
				.andDo(print());
		}

		@Test
		@WithMockUser
		@DisplayName("존재하지 않는 댓글 404")
		void notFoundComment() throws Exception {
			// given
			Comment comment = commentMockData.createComment(user);

			// when & then
			mockMvc.perform(delete("/comments/{commentId}", comment.getId() + 1)
					.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
				.andExpect(status().isNotFound())
				.andDo(print());
		}
	}
}
