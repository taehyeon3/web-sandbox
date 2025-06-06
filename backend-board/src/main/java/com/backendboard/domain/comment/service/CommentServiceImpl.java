package com.backendboard.domain.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.dto.CommentReadResponse;
import com.backendboard.domain.comment.dto.CommentSliceResponse;
import com.backendboard.domain.comment.dto.CommentUpdateRequest;
import com.backendboard.domain.comment.dto.CommentUpdateResponse;
import com.backendboard.domain.comment.entity.Comment;
import com.backendboard.domain.comment.repository.CommentQueryRepository;
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
	private final CommentQueryRepository commentQueryRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public CommentCreateResponse createComment(CommentCreateRequest request, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Comment comment = CommentCreateRequest.toEntity(request, user.getId());
		Comment saved = commentRepository.save(comment);
		return CommentCreateResponse.toDto(saved, user.getNickname());
	}

	@Transactional
	@Override
	public CommentUpdateResponse updateComment(CommentUpdateRequest request, Long commentId, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(CustomError.COMMENT_NOT_FOUND));

		validateAuthor(comment, user);

		comment.update(request.getContent());
		return CommentUpdateResponse.toDto(comment, user.getNickname());
	}

	@Override
	public CommentReadResponse getComment(Long commentId) {
		return commentQueryRepository.findCommentReadResponse(commentId);
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(CustomError.COMMENT_NOT_FOUND));

		validateAuthor(comment, user);

		comment.delete();
	}

	@Override
	public Slice<CommentSliceResponse> getCommetsSlice(Long postId, Pageable pageable) {
		return commentQueryRepository.findCommentSliceResponse(postId, false, pageable);
	}

	public void validateAuthor(Comment comment, User user) {
		if (comment.getUserId() != user.getId()) {
			throw new CustomException(CustomError.COMMENT_NOT_AUTHOR);
		}
	}
}
