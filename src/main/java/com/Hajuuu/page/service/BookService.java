package com.Hajuuu.page.service;

import com.Hajuuu.page.api.SearchBookDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public long saveBook(SearchBookDTO searchBookDTO) {
        Book book = new Book();
        book.createBook(searchBookDTO.getTitle(), searchBookDTO.getAuthor(), searchBookDTO.getPage(),
                searchBookDTO.getImage());
        bookRepository.save(book);
        return book.getId();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findBooks(User user) {
        return bookRepository.findAllById(user.getId());
    }

}
