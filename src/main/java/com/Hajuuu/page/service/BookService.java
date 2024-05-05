package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.SearchBookDTO;
import com.Hajuuu.page.controller.SessionConst;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttribute;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private final BookRepository bookRepository;

    @Transactional
    public long saveBook(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                         SearchBookDTO searchBookDTO) {
        Book book = new Book();
        book.createBook(loginUser, searchBookDTO.getTitle(), searchBookDTO.getAuthor(), searchBookDTO.getPage(),
                searchBookDTO.getImage(), searchBookDTO.getIsbn(), searchBookDTO.getBookState());
        validateDuplicateBook(loginUser.getId(), searchBookDTO.getIsbn());
        bookRepository.save(book);
        return book.getId();
    }

    private void validateDuplicateBook(int userId, String isbn) {
        List<Book> findBooks = bookRepository.findAllById(userId);
        if (findBooks.stream().filter(b -> b.getIsbn().equals(isbn)).count() > 0) {
            throw new IllegalStateException("이미 저장한 책이에요!");
        }
    }

    public Book findOne(int id) {
        return bookRepository.findOne(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findBooks(User user) {
        return bookRepository.findAllById(user.getId());
    }


}
