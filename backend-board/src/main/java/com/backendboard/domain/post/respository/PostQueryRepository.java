package com.backendboard.domain.post.respository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.post.dto.PostSliceResponse;
import com.backendboard.domain.post.dto.QPostSliceResponse;
import com.backendboard.domain.post.entity.QPost;
import com.backendboard.domain.user.entity.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
	private final JPAQueryFactory queryFactory;

	public Slice<PostSliceResponse> findPostSliceResponse(boolean deleted, Pageable pageable) {
		QPost post = QPost.post;
		QUser user = QUser.user;
		boolean hasNext = false;
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, post);

		List<PostSliceResponse> content = queryFactory
			.select(new QPostSliceResponse(
				post.id,
				user.nickname,
				post.title,
				post.content,
				post.likeCount,
				post.viewCount,
				post.createdDate,
				post.lastModifiedDate
			))
			.from(post)
			.join(user).on(post.userId.eq(user.id))
			.where(post.deleted.eq(deleted))
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

	private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QPost post) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();

		for (Sort.Order order : pageable.getSort()) {
			PathBuilder<?> entityPath = new PathBuilder<>(post.getType(), post.getMetadata());
			orders.add(
				new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, entityPath.get(order.getProperty()))
			);
		}
		return orders;
	}
}
