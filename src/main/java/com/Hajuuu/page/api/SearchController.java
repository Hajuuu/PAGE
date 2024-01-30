package com.Hajuuu.page.api;

import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.Post;
import com.Hajuuu.page.service.BookService;
import com.Hajuuu.page.service.PostService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final NaverSearchService naverSearchService;
    private final AladinSearchService aladinSearchService;

    private final PostService postService;
    private final BookService bookService;

    @GetMapping("/search/{title}")
    public NaverBookDTO getNaver(@PathVariable("title") String title, Model model) throws IOException {
        NaverBookDTO naverBooks = naverSearchService.getBookInfo(title);
        model.addAttribute(naverBooks);
        return naverBooks;
    }

    @PostMapping("search/{isbn}/select")
    public void selectBook(@PathVariable("isbn") String isbn, Model model) throws IOException {
        AladinBookDTO aladin = getAladin(isbn, model);
        Book book = new Book();
        List<AladinBookInfo> items = aladin.getItem();
        String author = "";
        String title = "";
        Long page = 0L;
        for (AladinBookInfo item : items) {
            author = item.getAuthor();
            title = item.getTitle();
            page = Long.valueOf(item.getSubInfo().getItemPage());
        }
        book.createBook(title, author, page);
        Long bookId = bookService.saveBook(book);
        Post post = new Post();
        post.setBook(book);
        postService.savePost(post);
    }

//    @PostMapping("/search/save")
//    public void savePost() {
//        String content = "sjaejgjwe;gjwelgjlwejglewjgalejwgljewlgjlegajelagjlejgwjegaewj";
//
//    }

//    @PostMapping("search/save")
//    public String saveBook() {
//
//    }

    @GetMapping("searchDetail/{isbn13}")
    public AladinBookDTO getAladin(@PathVariable("isbn13") String isbn13, Model model) throws IOException {
        AladinBookDTO aladinBookDTO = aladinSearchService.getBookInfo(isbn13);
        model.addAttribute(aladinBookDTO);
        return aladinBookDTO;
    }
}
