package com.Hajuuu.page.repository;

import com.Hajuuu.page.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select i from Post i", Post.class).getResultList();
    }

    public List<Post> findAllById(Long bookId) {
        return em.createQuery("select i from Post i where i.book.id = :bookId", Post.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }
}
