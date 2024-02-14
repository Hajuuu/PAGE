package com.Hajuuu.page.repository;

import com.Hajuuu.page.domain.Book;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final EntityManager em;

    public void save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
        } else {
            em.merge(book);
        }
    }

    public Book findOne(Long id) {
        return em.find(Book.class, id);
    }

    public List<Book> findAll() {
        return em.createQuery("select i from Book i", Book.class).getResultList();
    }

    public List<Book> findAllById(Long userId) {
        return em.createQuery("select i from Book i where i.user.id = :userId", Book.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
