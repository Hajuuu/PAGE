package com.Hajuuu.page.repository;

import static com.Hajuuu.page.domain.QPost.post;
import static org.hibernate.internal.util.StringHelper.isEmpty;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.PostFormDTO;
import com.Hajuuu.page.DTO.QPostDTO;
import com.Hajuuu.page.DTO.QPostFormDTO;
import com.Hajuuu.page.domain.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<PostDTO> findPostsByFollowingIds(List<Integer> followingIds) {
        QPost post = QPost.post;

        return queryFactory
                .select(new QPostDTO(
                        post.book.title,       // bookTitle
                        post.content,          // content
                        post.createdTime,      // createdTime
                        post.user.loginId,      // loginId
                        post.startPage,
                        post.endPage
                ))
                .from(post)
                .where(post.user.id.in(followingIds))
                .orderBy(post.createdTime.desc())
                .fetch();
    }

    @Override
    public List<PostFormDTO> search(int bookId) {
        return queryFactory
                .select(new QPostFormDTO(
                        post.content,
                        post.createdTime,
                        post.book.id,
                        post.startPage,
                        post.endPage))
                .from(post)
                .where(bookIdEq(bookId))
                .fetch();
    }

    private BooleanExpression bookIdEq(int bookId) {
        return isEmpty(String.valueOf(bookId)) ? null : post.book.id.eq(bookId);
    }

    @Override
    public long countTotalBooks(int userId) {
        return queryFactory
                .select(post.book.id.countDistinct())
                .from(post)
                .where(post.user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public long sumTotalPages(int userId) {
        Long result = queryFactory
                .select(post.endPage.subtract(post.startPage).add(1).sum())
                .from(post)
                .where(post.user.id.eq(userId))
                .fetchOne();

        return result != null ? result : 0L;
    }

    @Override
    public double avgPagesThisMonth(int userId) {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDateTime start = firstDay.atStartOfDay();

        Long result = queryFactory
                .select(post.endPage.subtract(post.startPage).add(1).sum())
                .from(post)
                .where(
                        post.user.id.eq(userId),
                        post.createdTime.after(start)
                )
                .fetchOne();

        long totalPagesThisMonth = (result != null ? result : 0L);
        int dayOfMonth = LocalDate.now().getDayOfMonth();

        // 평균 페이지 수
        return dayOfMonth > 0 ? (double) totalPagesThisMonth / dayOfMonth : 0;
    }
}
