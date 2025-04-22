package com.backendboard.global.config;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class WebConfigTest {
	@Autowired
	private MockMvc mockMvc;

	@Value("${spring.file.upload.directory}")
	private String uploadDirectory;

	private static final String TEST_IMAGE_NAME = "test-image.jpg";

	@BeforeEach
	public void setup() throws IOException {
		File directory = new File(uploadDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File testImage = new File(directory, TEST_IMAGE_NAME);
		if (!testImage.exists()) {
			BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.RED);
			g.fillRect(0, 0, 100, 100);
			g.dispose();
			ImageIO.write(image, "jpg", testImage);
		}
	}

	@AfterEach
	public void cleanup() {
		File testImage = new File(uploadDirectory, TEST_IMAGE_NAME);
		if (testImage.exists()) {
			testImage.delete();
		}
	}

	@DisplayName("이미지 파일이 존재하는지 테스트")
	@Test
	public void resourceHandlerConfiguration() throws Exception {
		File imageFile = new File(uploadDirectory, TEST_IMAGE_NAME);
		assumeTrue(imageFile.exists(), "테스트 이미지 파일이 존재해야 합니다: " + imageFile.getAbsolutePath());

		mockMvc.perform(get("/images/" + TEST_IMAGE_NAME))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().exists(HttpHeaders.CONTENT_TYPE));
	}

	@DisplayName("캐시가 제대로 적용 되었는지 테스트")
	@Test
	public void cacheControlHeader() throws Exception {
		File imageFile = new File(uploadDirectory, TEST_IMAGE_NAME);
		assumeTrue(imageFile.exists(), "테스트 이미지 파일이 존재해야 합니다: " + imageFile.getAbsolutePath());

		MvcResult result = mockMvc.perform(get("/images/" + TEST_IMAGE_NAME))
			.andExpect(status().isOk())
			.andExpect(header().exists(HttpHeaders.CACHE_CONTROL))
			.andReturn();

		String cacheControl = result.getResponse().getHeader(HttpHeaders.CACHE_CONTROL);
		assertThat(cacheControl).contains("max-age=3600");
	}

	@DisplayName("이미지 파일이 존재하지 않은 경우")
	@Test
	public void nonExistentResource() throws Exception {
		mockMvc.perform(get("/images/non-existent-image.jpg"))
			.andExpect(status().isNotFound());
	}

	@DisplayName("상위 경로로 접근하는 경우")
	@Test
	public void directoryTraversalProtection() throws Exception {
		mockMvc.perform(get("/images/../../../etc/passwd"))
			.andExpect(status().isBadRequest());
	}
}
