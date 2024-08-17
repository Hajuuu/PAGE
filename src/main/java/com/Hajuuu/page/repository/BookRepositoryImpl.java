package com.Hajuuu.page.repository;

import static com.Hajuuu.page.domain.QBook.book;
import static org.hibernate.internal.util.StringHelper.isEmpty;

import com.Hajuuu.page.DTO.QSearchBookDTO;
import com.Hajuuu.page.DTO.SearchBookDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;

public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SearchBookDTO> search(int userId) {
        return queryFactory
                .select(new QSearchBookDTO(
                        book.id,
                        book.title,
                        book.image,
                        book.page,
                        book.author,
                        book.isbn,
                        book.bookState))
                .from(book)
                .where(book.user.id.eq(userId))
                .fetch();
    }

    private BooleanExpression userIdEq(int userId) {
        return isEmpty(String.valueOf(userId)) ? null : book.user.id.eq(userId);
    }
}
