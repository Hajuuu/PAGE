package com.Hajuuu.page.controller;

import com.Hajuuu.page.api.AladinBookDTO;
import com.Hajuuu.page.api.AladinBookInfo;
import com.Hajuuu.page.api.AladinSearchService;
import com.Hajuuu.page.api.NaverBookDTO;
import com.Hajuuu.page.api.NaverBookInfo;
import com.Hajuuu.page.api.NaverSearchService;
import com.Hajuuu.page.api.SearchBookDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.service.BookService;
import com.Hajuuu.page.service.PostService;
import com.Hajuuu.page.service.UserService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final NaverSearchService naverSearchService;
    private final AladinSearchService aladinSearchService;

    private final PostService postService;
    private final BookService bookService;
    private final UserService userService;

    private static String image = "";

    @GetMapping("/search/naverBook")
    public String searchTitle(Model model)
            throws IOException {
        model.addAttribute(new NaverBookInfo());
        return "/search/naverBook";
    }

    @PostMapping("/search/naverBook")
    public String search(@ModelAttribute("title") String title, @ModelAttribute("items") NaverBookInfo naverBookInfo,
                         Model model)
            throws IOException {
        if (title == null || title.isBlank() || title.isEmpty()) {
            return "/search/naverBook";
        }
        NaverBookDTO bookInfo = naverSearchService.getBookInfo(title);

        List<NaverBookInfo> items = bookInfo.getItems();
        model.addAttribute("items", items);
        model.addAttribute("title", title);
        return "/search/naverBook";
    }

    @GetMapping("/book/new")
    public String searchBook(@ModelAttribute("books") SearchBookDTO searchBookDTO, Model model) {
        model.addAttribute("books", searchBookDTO);

        return "/search/createBookForm";
    }

    @PostMapping("/book/new")
    public String createBook(@RequestParam("title") String title, @ModelAttribute("books") SearchBookDTO searchBookDTO,
                             BindingResult bindingResult,
                             Model model) {

        NaverBookDTO bookInfo = naverSearchService.getBookInfo(title);
        List<NaverBookInfo> naverBookInfos = bookInfo.getItems();
        String image = "";
        for (NaverBookInfo item : naverBookInfos) {
            image = item.getImage();
        }
        //검증 로직
//        if (!StringUtils.hasText(searchBookDTO.getTitle())) {
//            bindingResult.addError(new FieldError("searchBookDTO", "title", "제목을 입력해주세요!"));
//        }
//        if (!StringUtils.hasText(searchBookDTO.getAuthor())) {
//            bindingResult.addError(new FieldError("searchBookDTO", "author", "저자를 입력해주세요!"));
//        }
//        if (searchBookDTO.getPage() < 1) {
//            bindingResult.addError(new FieldError("searchBookDTO", "page", "페이지는 1장 이상 입력해주세요"));
//        }
//
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "search/createBookForm";
//        }
        searchBookDTO.setImage(image);
        bookService.saveBook(searchBookDTO);

        return "redirect:/my/books";
    }

    @GetMapping("/my/books")
    public String list(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "/my/books";
    }


    @PostMapping(value = "/book/{isbn}/select")
    public String selectBook(@PathVariable("isbn") String isbn, RedirectAttributes redirectAttributes,
                             Model model)
            throws IOException {

        AladinBookDTO aladinBookDTO = aladinSearchService.getBookInfo(isbn);
        NaverBookDTO bookInfo = naverSearchService.getBookInfo(isbn);
        List<NaverBookInfo> naverBookInfos = bookInfo.getItems();
        List<AladinBookInfo> aladinBookInfos = aladinBookDTO.getItem();
        Long page = 0L;
        String title = "";
        String author = "";
        String image = "";
        String publisher = "";
        for (AladinBookInfo item : aladinBookInfos) {
            page = Long.valueOf(item.getSubInfo().getItemPage());
        }
        for (NaverBookInfo item : naverBookInfos) {
            title = item.getTitle();
            author = item.getAuthor();
            image = item.getImage();
            publisher = item.getPublisher();
        }
        SearchBookDTO searchBookDTO = new SearchBookDTO();
        searchBookDTO.setPage(page);
        searchBookDTO.setImage(image);
        searchBookDTO.setIsbn(isbn);
        searchBookDTO.setTitle(title);
        searchBookDTO.setAuthor(author);
        redirectAttributes.addFlashAttribute("books", searchBookDTO);
        log.info(searchBookDTO.getImage());
        log.info(title);
        return "redirect:/book/new";
    }

    @PostMapping("/my/{id}/posts")
    public String addPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", new Book());
        return "/my/createPostForm";
    }

}
