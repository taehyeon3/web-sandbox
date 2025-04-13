package com.backendboard.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public GroupedOpenApi image() {
		return GroupedOpenApi.builder()
			.group("이미지 관련 API")
			.pathsToMatch("/images/**")
			.build();
	}

	@Bean
	public GroupedOpenApi comment() {
		return GroupedOpenApi.builder()
			.group("댓글 관련 API")
			.pathsToMatch("/comments/**")
			.build();
	}

	@Bean
	public GroupedOpenApi auth() {
		return GroupedOpenApi.builder()
			.group("인증 관련 API")
			.pathsToMatch("/join", "reissue")
			.build();
	}

	@Bean
	public GroupedOpenApi all() {
		return GroupedOpenApi.builder()
			.group("All")
			.pathsToMatch("/**")
			.build();
	}

	@Bean
	public OpenAPI openAPI() {
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
			.addServersItem(new Server().description("로컬 서버"))
			.info(new Info()
				.title("샌드박스 API 문서")
				.description("샌드박스 API 명세서")
				.version("1.0.0"));
	}
}
