package com.backendboard.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.dto.CommentUpdateRequest;
import com.backendboard.domain.comment.dto.CommentUpdateResponse;
import com.backendboard.domain.comment.entity.Comment;
import com.backendboard.domain.comment.repository.CommentRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public CommentCreateResponse createComment(CommentCreateRequest request, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Comment comment = CommentCreateRequest.toEntity(request, user.getNickname());
		Comment saved = commentRepository.save(comment);
		return CommentCreateResponse.toDto(saved);
	}

	@Transactional
	@Override
	public CommentUpdateResponse updateComment(CommentUpdateRequest request, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(CustomError.COMMENT_NOT_FOUND));
		comment.updateContent(request.getContent());
		return CommentUpdateResponse.toDto(comment);
	}
}
