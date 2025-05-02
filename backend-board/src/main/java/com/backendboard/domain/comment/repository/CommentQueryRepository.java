package com.backendboard.domain.comment.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.comment.dto.CommentReadResponse;
import com.backendboard.domain.comment.dto.CommentSliceResponse;
import com.backendboard.domain.comment.dto.QCommentReadResponse;
import com.backendboard.domain.comment.dto.QCommentSliceResponse;
import com.backendboard.domain.comment.entity.QComment;
import com.backendboard.domain.user.entity.QUser;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
	private final JPAQueryFactory queryFactory;

	public CommentReadResponse findCommentReadResponse(Long commentId) {
		QComment comment = QComment.comment;
		QUser user = QUser.user;

		CommentReadResponse result = queryFactory
			.select(new QCommentReadResponse(
				comment.id,
				comment.postId,
				user.nickname,
				comment.content,
				comment.createdDate,
				comment.lastModifiedDate
			))
			.from(comment)
			.join(user).on(comment.userId.eq(user.id))
			.where(comment.id.eq(commentId))
			.fetchOne();

		if (result == null) {
			throw new CustomException(CustomError.COMMENT_NOT_FOUND);
		}

		return result;
	}

	public Slice<CommentSliceResponse> findCommentSliceResponse(Long postId, boolean deleted, Pageable pageable) {
		QComment comment = QComment.comment;
		QUser user = QUser.user;
		boolean hasNext = false;
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, comment);

		List<CommentSliceResponse> content = queryFactory
			.select(new QCommentSliceResponse(
				comment.id,
				comment.postId,
				user.nickname,
				comment.content,
				comment.createdDate,
				comment.lastModifiedDate
			))
			.from(comment)
			.join(user).on(comment.userId.eq(user.id))
			.where(
				comment.postId.eq(postId),
				comment.deleted.eq(deleted)
			)
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageable, hasNext);
	}

	private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QComment comment) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();

		for (Sort.Order order : pageable.getSort()) {
			PathBuilder<?> entityPath = new PathBuilder<>(comment.getType(), comment.getMetadata());
			orders.add(
				new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, entityPath.get(order.getProperty()))
			);
		}
		return orders;
	}

}
