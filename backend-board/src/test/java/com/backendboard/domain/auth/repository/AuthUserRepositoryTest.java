package com.backendboard.domain.auth.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;

@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
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
			AuthUser authUser = AuthUser.create(username, "1234", UserRole.USER);
			authUserRepository.save(authUser);

			//when
			boolean exists = authUserRepository.existsByUsername(username);

			//then
			Assertions.assertThat(exists).isTrue();
		}
	}

}
