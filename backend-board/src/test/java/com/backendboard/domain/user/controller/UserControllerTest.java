package com.backendboard.domain.user.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.user.UserMockData;
import com.backendboard.domain.user.dto.JoinRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserMockData userMockData;

	@Nested
	@DisplayName("회원가입 API 테스트")
	class JoinTest {
		private JoinRequest joinRequest;

		@BeforeEach
		void setUp() {
			joinRequest = JoinRequest.builder()
				.loginId("testId")
				.password("1234")
				.username("potato")
				.nickname("testNick")
				.build();
		}

		@Transactional
		@Test
		@DisplayName("성공 201")
		void success() throws Exception {
			// given

			// when & then
			mockMvc.perform(post("/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(joinRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.loginId").value("testId"))
				.andExpect(jsonPath("$.username").value("potato"))
				.andExpect(jsonPath("$.nickname").value("testNick"));
		}

		@Transactional
		@Test
		@DisplayName("아이디 중복 실패 403")
		void duplicateIdFailure() throws Exception {
			// given
			userMockData.createUserAndAuthUser(joinRequest);

			// when & then
			mockMvc.perform(post("/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(joinRequest)))
				.andExpect(status().isForbidden());
		}

		@Transactional
		@Test
		@DisplayName("닉네임 중복 실패 403")
		void duplicateNicknameFailure() throws Exception {
			// given
			userMockData.createUserAndAuthUser(joinRequest);

			// when & then
			mockMvc.perform(post("/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(joinRequest)))
				.andExpect(status().isForbidden());
		}

	}
}
