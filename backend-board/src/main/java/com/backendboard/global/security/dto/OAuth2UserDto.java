package com.backendboard.global.security.dto;

import com.backendboard.domain.auth.entity.type.UserRole;

public record OAuth2UserDto(String username, String name, UserRole role) {
}
