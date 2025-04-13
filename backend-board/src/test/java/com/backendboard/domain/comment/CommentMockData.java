package com.backendboard.domain.comment;

import org.springframework.stereotype.Component;

import com.backendboard.domain.comment.entity.Comment;
import com.backendboard.domain.comment.repository.CommentRepository;
import com.backendboard.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentMockData {
	private final CommentRepository commentRepository;

	public Comment createComment(User user) {
		Comment comment = Comment.builder().postId(1L).content("안녕하세요").userId(user.getId()).build();
		return commentRepository.save(comment);
	}
}
