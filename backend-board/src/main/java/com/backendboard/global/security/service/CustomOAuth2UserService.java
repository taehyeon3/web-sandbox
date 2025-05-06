package com.backendboard.global.security.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.auth.repository.AuthUserRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.security.dto.CustomOAuth2User;
import com.backendboard.global.security.dto.KakaoOAuth2Response;
import com.backendboard.global.security.dto.OAuth2Response;
import com.backendboard.global.security.dto.OAuth2UserDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final AuthUserRepository authUserRepository;

	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoOAuth2Response(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String username = oAuth2Response.getProvider() + "#" + oAuth2Response.getProviderId();
		String name = oAuth2Response.getName();
		AuthUser authUser = authUserRepository.findObjectByUsername(username);
		OAuth2UserDto oAuth2UserDto = null;

		if (authUser == null) {
			AuthUser newAuthUser = AuthUser.create(username, "", UserRole.USER);
			User newUser = newAuthUser.createUser(name, name + "#" + oAuth2Response.getProvider());
			authUserRepository.save(newAuthUser);
			userRepository.save(newUser);
			oAuth2UserDto = new OAuth2UserDto(username, name, UserRole.USER);
		} else {
			oAuth2UserDto = new OAuth2UserDto(username, name, authUser.getRole());
		}

		return new CustomOAuth2User(oAuth2UserDto);
	}
}
