package com.backendboard.global.security.dto;

import java.util.Map;

public class KakaoOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attribute;

	public KakaoOAuth2Response(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

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
		return attribute.get("name").toString();
	}
}
