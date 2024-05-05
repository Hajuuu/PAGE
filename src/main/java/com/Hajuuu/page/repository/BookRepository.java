package com.Hajuuu.page.repository;

import com.Hajuuu.page.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Book book) {
        em.persist(book);
    }

    public Book findOne(int id) {
        return em.find(Book.class, id);
    }

    public List<Book> findAll() {
        return em.createQuery("select i from Book i", Book.class).getResultList();
    }

    public List<Book> findAllById(int userId) {
        return em.createQuery("select i from Book i where i.user.id = :userId", Book.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Book> findAllByTitle(String title) {
        return em.createQuery("select i from Book i where i.title = :title", Book.class)
                .setParameter("title", title)
                .getResultList();
    }
}
