package com.backendboard.global.security.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.global.security.dto.CustomOAuth2User;
import com.backendboard.global.security.dto.KakaoOAuth2Response;
import com.backendboard.global.security.dto.OAuth2Response;
import com.backendboard.global.security.dto.OAuth2UserDto;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		System.out.println("oAuth2User = " + oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoOAuth2Response(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(username, oAuth2Response.getName(), UserRole.USER);
		return new CustomOAuth2User(oAuth2UserDto);
	}
}
