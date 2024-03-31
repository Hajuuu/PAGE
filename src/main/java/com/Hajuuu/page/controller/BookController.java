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
                          BindingResult bindingResult, Model model) {

        //검증 로직
        if (!StringUtils.hasText(searchBookDTO.getImage())) {
            bindingResult.addError(new FieldError("searchBookDTO", "image", searchBookDTO.getImage(), false, null, null,
                    "이미지를 입력해주세요!"));
        }
        if (!StringUtils.hasText(searchBookDTO.getTitle())) {
            bindingResult.addError(new FieldError("searchBookDTO", "title", searchBookDTO.getTitle(), false, null, null,
                    "제목을 입력해주세요!"));
        }
        if (!StringUtils.hasText(searchBookDTO.getAuthor())) {
            bindingResult.addError(
                    new FieldError("searchBookDTO", "author", searchBookDTO.getAuthor(), false, null, null,
                            "저자를 입력해주세요!"));
        }
        if (!StringUtils.hasText(searchBookDTO.getIsbn())) {
            bindingResult.addError(new FieldError("searchBookDTO", "isbn", searchBookDTO.getIsbn(), false, null, null,
                    "isbn을 입력해주세요!"));
        }
        if (searchBookDTO.getPage() < 1) {
            bindingResult.addError(new FieldError("searchBookDTO", "page", searchBookDTO.getPage(), false,
                    new String[]{"min.book.page"}, null, null));
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            searchBookDTO.setImage(image);
            return "search/createBookForm";
        }

        searchBookDTO.setImage(image);
        bookService.saveBook(loginUser, searchBookDTO);

        return "redirect:/books";
    }

    @PostMapping(value = "/book/{isbn}/select")
    public String selectBook(@PathVariable("isbn") String isbn, RedirectAttributes redirectAttributes) {
        List<NaverBookInfo> naverBookInfos = naverSearchService.getBookInfo(isbn).getItems();
        image = naverBookInfos.get(0).getImage();
        SearchBookDTO searchBookDTO = SearchBookDTO.builder()
                .title(naverBookInfos.get(0).getTitle())
                .author(naverBookInfos.get(0).getAuthor())
                .image(image)
                .isbn(isbn)
                .page(getAladinInfo(isbn)).build();

        redirectAttributes.addFlashAttribute("books", searchBookDTO);
        return "redirect:/book/new";
    }

    private Long getAladinInfo(String isbn) {
        AladinBookDTO aladinBookDTO = aladinSearchService.getBookInfo(isbn);
        List<AladinBookInfo> aladinBookInfos = aladinBookDTO.getItem();
        Long page = 0L;
        for (AladinBookInfo item : aladinBookInfos) {
            page = Long.valueOf(item.getSubInfo().getItemPage());
        }
        return page;
    }

}
