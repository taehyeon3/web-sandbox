package com.backendboard.global.security.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.backendboard.domain.auth.entitiy.AuthUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final AuthUser authUser;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(authUser.getRole().getValue()));
	}

	@Override
	public String getUsername() {
		return authUser.getUsername();
	}

	@Override
	public String getPassword() {
		return authUser.getPassword();
	}

	public Long getId() {
		return authUser.getId();
	}

	@Override
	public boolean isEnabled() {
		return authUser.isStatus();
	}

	//계정 만료 여부
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//계정 잠금 여부
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//비밀번호 만료 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
