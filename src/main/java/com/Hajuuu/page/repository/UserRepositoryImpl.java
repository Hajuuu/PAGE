package com.Hajuuu.page.repository;

import static com.Hajuuu.page.domain.QPost.post;
import static com.Hajuuu.page.domain.QUser.user;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.QPostDTO;
import com.Hajuuu.page.DTO.QSearchUserDTO;
import com.Hajuuu.page.DTO.SearchUserDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SearchUserDTO> search(String loginId) {
        return queryFactory
                .select(new QSearchUserDTO(
                        user.id,
                        user.loginId
                ))
                .from(user)
                .where(user.loginId.contains(loginId))
                .fetch();
    }

    @Override
    public List<PostDTO> following(List<Integer> followerList) {
        return queryFactory
                .select(new QPostDTO(
                        post.book.title,
                        post.content,
                        post.createdTime,
                        post.user.loginId
                ))
                .from(post)
                .where(post.user.id.in(followerList))
                .fetch();

    }

}
