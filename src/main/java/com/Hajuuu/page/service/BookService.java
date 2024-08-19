package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.BookFormDTO;
import com.Hajuuu.page.DTO.SearchBookDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Transactional
    public long saveBook(User loginUser,
                         BookFormDTO bookFormDTO) {
        Book book = new Book();
        book.createBook(loginUser, bookFormDTO.getTitle(), bookFormDTO.getAuthor(), bookFormDTO.getPage(),
                bookFormDTO.getImage(), bookFormDTO.getIsbn(), bookFormDTO.getBookState());
        validateDuplicateBook(loginUser.getId(), bookFormDTO.getIsbn());
        bookRepository.save(book);
        return book.getId();
    }

    private void validateDuplicateBook(int userId, String isbn) {
        List<SearchBookDTO> findBooks = bookRepository.search(userId);
        if (findBooks.stream().filter(b -> b.getIsbn().equals(isbn)).count() > 0) {
            throw new IllegalStateException("이미 저장한 책이에요!");
        }
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<SearchBookDTO> search(int userId) {
        return bookRepository.search(userId);
    }


}
