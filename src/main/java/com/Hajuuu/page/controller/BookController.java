package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.SearchBookDTO;
import com.Hajuuu.page.api.AladinBookDTO;
import com.Hajuuu.page.api.AladinBookInfo;
import com.Hajuuu.page.api.AladinSearchService;
import com.Hajuuu.page.api.NaverBookInfo;
import com.Hajuuu.page.api.NaverSearchService;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.BookService;
import com.Hajuuu.page.web.argumentresolver.Login;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final NaverSearchService naverSearchService;
    private final AladinSearchService aladinSearchService;
    private final BookService bookService;

    private static String image = "";

    @GetMapping("/book/new")
    public String bookForm(@ModelAttribute("books") SearchBookDTO searchBookDTO, Model model) {
        model.addAttribute("books", searchBookDTO);

        return "/search/createBookForm";
    }

    @PostMapping("/book/new")
    public String addBook(@Login User loginUser,
                          @ModelAttribute("books") SearchBookDTO searchBookDTO,
                          BindingResult bindingResult,
                          Model model) {

        //검증 로직
        if (!StringUtils.hasText(searchBookDTO.getTitle())) {
            bindingResult.addError(new FieldError("searchBookDTO", "title", "제목을 입력해주세요!"));
        }
        if (!StringUtils.hasText(searchBookDTO.getAuthor())) {
            bindingResult.addError(new FieldError("searchBookDTO", "author", "저자를 입력해주세요!"));
        }
        if (searchBookDTO.getPage() < 1) {
            bindingResult.addError(new FieldError("searchBookDTO", "page", "페이지는 1장 이상 입력해주세요"));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "search/createBookForm";
        }
        searchBookDTO.setImage(image);

        bookService.saveBook(loginUser, searchBookDTO);

        return "redirect:/books";
    }

    @PostMapping(value = "/book/{isbn}/select")
    public String selectBook(@PathVariable("isbn") String isbn,
                             RedirectAttributes redirectAttributes,
                             Model model)
            throws IOException {

        Long page = getAladinInfo(isbn);
        String title = "";
        String author = "";
        List<NaverBookInfo> naverBookInfos = naverSearchService.getBookInfo(isbn).getItems();

        for (NaverBookInfo item : naverBookInfos) {
            title = item.getTitle();
            author = item.getAuthor();
            image = item.getImage();
        }
        SearchBookDTO searchBookDTO = SearchBookDTO.builder().page(page).image(image).isbn(isbn).title(title)
                .author(author)
                .build();
        redirectAttributes.addFlashAttribute("books", searchBookDTO);
        return "redirect:/book/new";
    }

    private Long getAladinInfo(String isbn) throws IOException {
        AladinBookDTO aladinBookDTO = aladinSearchService.getBookInfo(isbn);
        List<AladinBookInfo> aladinBookInfos = aladinBookDTO.getItem();
        Long page = 0L;
        for (AladinBookInfo item : aladinBookInfos) {
            page = Long.valueOf(item.getSubInfo().getItemPage());
        }
        return page;
    }

}
