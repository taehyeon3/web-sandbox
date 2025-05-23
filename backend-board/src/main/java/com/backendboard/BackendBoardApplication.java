package com.backendboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class BackendBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendBoardApplication.class, args);
	}

}
