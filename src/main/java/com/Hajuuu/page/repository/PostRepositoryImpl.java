package com.Hajuuu.page.repository;

import static com.Hajuuu.page.domain.QPost.post;
import static org.hibernate.internal.util.StringHelper.isEmpty;

import com.Hajuuu.page.DTO.PostFormDTO;
import com.Hajuuu.page.DTO.QPostFormDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostFormDTO> search(int bookId) {
        return queryFactory
                .select(new QPostFormDTO(
                        post.content,
                        post.createdTime,
                        post.book.id,
                        post.page))
                .from(post)
                .where(bookIdEq(bookId))
                .fetch();
    }

    private BooleanExpression bookIdEq(int bookId) {
        return isEmpty(String.valueOf(bookId)) ? null : post.book.id.eq(bookId);
    }

}
