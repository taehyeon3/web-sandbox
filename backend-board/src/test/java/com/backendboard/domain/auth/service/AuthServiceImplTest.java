package com.backendboard.domain.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.backendboard.domain.auth.dto.JoinRequest;
import com.backendboard.domain.auth.dto.JoinResponse;
import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.auth.repository.AuthUserRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private AuthUserRepository authUserRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	private AuthServiceImpl userService;

	@Nested
	@DisplayName("회원가입 프로세스 테스트")
	class JoinProcessTest {

		private JoinRequest joinRequest;
		private User user;
		private AuthUser authUser;

		@BeforeEach
		void setUp() {
			joinRequest = JoinRequest.builder()
				.loginId("testId")
				.password("1234")
				.username("potato")
				.nickname("testNick")
				.build();
			authUser = AuthUser.create("potato", "encoded_password", UserRole.USER);
			user = authUser.createUser("testId", "testNick");
		}

		@Test
		@DisplayName("회원가입 요청 성공")
		void success() {
			// given
			when(userRepository.existsByNickname(anyString())).thenReturn(false);
			when(authUserRepository.existsByUsername(anyString())).thenReturn(false);
			when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encoded_password");

			when(userRepository.save(any(User.class))).thenReturn(user);
			when(authUserRepository.save(any(AuthUser.class))).thenReturn(authUser);

			// when
			JoinResponse response = userService.joinProcess(joinRequest);

			// then
			verify(userRepository).existsByNickname("testNick");
			verify(authUserRepository).existsByUsername("testId");
			verify(bCryptPasswordEncoder).encode("1234");
			verify(userRepository).save(any(User.class));
			verify(authUserRepository).save(any(AuthUser.class));

			assertThat(response).isNotNull();
		}

		@Test
		@DisplayName("중복된 아이디가 있으면 예외 발생")
		void throwsExceptionWhenIdDuplicated() {
			// given
			when(authUserRepository.existsByUsername(anyString())).thenReturn(true);

			// when & then
			CustomException exception = assertThrows(CustomException.class,
				() -> userService.joinProcess(joinRequest));

			assertThat(exception.getError()).isEqualTo(CustomError.USER_DUPLICATION_ID);
			verify(authUserRepository).existsByUsername("testId");
			verify(userRepository, never()).existsByNickname(anyString());
			verify(authUserRepository, never()).save(any());
			verify(userRepository, never()).save(any());
		}

		@Test
		@DisplayName("중복된 닉네임이 있으면 예외 발생")
		void throwsExceptionWhenNicknameDuplicated() {
			// given
			when(authUserRepository.existsByUsername(anyString())).thenReturn(false);
			when(userRepository.existsByNickname(anyString())).thenReturn(true);

			// when & then
			CustomException exception = assertThrows(CustomException.class,
				() -> userService.joinProcess(joinRequest));

			assertThat(exception.getError()).isEqualTo(CustomError.USER_DUPLICATION_NICKNAME);
			verify(authUserRepository).existsByUsername("testId");
			verify(userRepository).existsByNickname("testNick");
			verify(authUserRepository, never()).save(any());
			verify(userRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("검증 테스트")
	class ValidationTest {

		@Test
		@DisplayName("중복된 닉네임 검증 시 예외 발생")
		void validateDuplicationNickname() {
			// given
			String nickname = "testNick";
			when(userRepository.existsByNickname(nickname)).thenReturn(true);

			// when & then
			CustomException exception = assertThrows(CustomException.class,
				() -> userService.validateDuplicationNickname(nickname));

			assertThat(exception.getError()).isEqualTo(CustomError.USER_DUPLICATION_NICKNAME);
		}

		@Test
		@DisplayName("중복된 아이디 검증 시 예외 발생")
		void validateDuplicationId() {
			// given
			String loginId = "testId";
			when(authUserRepository.existsByUsername(loginId)).thenReturn(true);

			// when & then
			CustomException exception = assertThrows(CustomException.class,
				() -> userService.validateDuplicationId(loginId));

			assertThat(exception.getError()).isEqualTo(CustomError.USER_DUPLICATION_ID);
		}
	}
}
