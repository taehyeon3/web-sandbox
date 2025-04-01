package com.backendboard.domain.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backendboard.domain.user.entitiy.AuthUser;
import com.backendboard.domain.user.entitiy.type.UserRole;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthUserRepositoryTest {
	@Autowired
	AuthUserRepository authUserRepository;

	@Nested
	@DisplayName("existsByUsername 메서드 테스트")
	class ExistsByUsernameMethodTest {
		@Test
		@DisplayName("username이 중복이면 true 반환")
		void returnsTrueWhenUsernameExists() {
			//given
			String username = "potato";
			AuthUser authUser = new AuthUser(username, "1234", UserRole.USER);
			authUserRepository.save(authUser);

			//when
			boolean exists = authUserRepository.existsByUsername(username);

			//then
			Assertions.assertThat(exists).isTrue();
		}
	}

}
