package com.backendboard.domain.post;

import org.springframework.stereotype.Component;

import com.backendboard.domain.post.entity.Post;
import com.backendboard.domain.post.respository.PostRepository;
import com.backendboard.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostMockData {
	private final PostRepository postRepository;

	public Post createPost(User user) {
		Post post = Post.builder().title("제목 입니다.").content("내용 입니다.").userId(user.getId()).build();
		return postRepository.save(post);
	}
}
