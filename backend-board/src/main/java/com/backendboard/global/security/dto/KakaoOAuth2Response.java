package com.backendboard.global.security.dto;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attribute;

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getName() {
		return ((Map<String, Object>)attribute.get("properties")).get("nickname").toString();
	}
}
