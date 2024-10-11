package com.Hajuuu.page.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceTest {


    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    SearchBookDTO book1 = new SearchBookDTO();
    SearchBookDTO book2 = new SearchBookDTO();

    User user = new User();

    void init() {
        book1.setTitle("abc");
        book1.setImage("asdfsdfsdfas");
        book1.setPage(230L);
        book1.setBookState(BookState.READING);
        book1.setIsbn("14342314");
        book1.setAuthor("asdf");

        book2.setTitle("abc");
        book2.setImage("asdfsasdfdfsdfas");
        book2.setPage(230L);
        book2.setBookState(BookState.READING);
        book2.setIsbn("143425314");
        book2.setAuthor("asddf");

        user.createUser("qwer789", "qwer789");

    }

    @Test
    void 책_중복_확인() {
        //Given
        init();

        //When
        bookService.saveBook(user, book1);

        //Then
        assertThatThrownBy(() -> bookService.saveBook(user, book2)).isInstanceOf(IllegalStateException.class);
    }

}