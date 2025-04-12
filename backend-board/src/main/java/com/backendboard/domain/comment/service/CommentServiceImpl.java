package com.backendboard.domain.comment.service;

import org.springframework.stereotype.Service;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.entity.Comment;
import com.backendboard.domain.comment.repository.CommentRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	@Override
	public CommentCreateResponse createComment(CommentCreateRequest request, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Comment comment = CommentCreateRequest.toEntity(request, user.getNickname());
		Comment saved = commentRepository.save(comment);
		return CommentCreateResponse.toDto(saved);
	}
}
