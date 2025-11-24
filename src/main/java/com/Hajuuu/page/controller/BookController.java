package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.BookFormDTO;
import com.Hajuuu.page.api.*;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.security.CustomUserDetails;
import com.Hajuuu.page.service.BookService;
import com.Hajuuu.page.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final NaverSearchService naverSearchService;
    private final AladinSearchService aladinSearchService;
    private final UserService userService;
    private final BookService bookService;

    private static String image = "";

    @GetMapping("/book/new")
    public String bookForm(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("books") BookFormDTO bookFormDTO, Model model) {
        if (userDetails == null) {
            model.addAttribute("user", null);
            return "home";
        }
        String loginId = userDetails.getUsername();
        User loginUser = userService.findByLoginId(loginId);
        model.addAttribute("user", loginUser);
        model.addAttribute("books", bookFormDTO);

        return "/search/createBookForm";
    }

    @PostMapping("/book/new")
    public String addBook(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("books") BookFormDTO bookFormDTO,
                          BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            bookFormDTO.setImage(image);
            return "search/createBookForm";
        }

        if (userDetails == null) {
            model.addAttribute("user", null);
            return "home";
        }
        String loginId = userDetails.getUsername();
        User loginUser = userService.findByLoginId(loginId);
        bookFormDTO.setImage(image);
        bookService.saveBook(loginUser, bookFormDTO);

        redirectAttributes.addFlashAttribute("user", loginUser);
        return "redirect:/books";
    }

    @PostMapping(value = "/book/{isbn}/select")
    public String selectBook(@PathVariable("isbn") String isbn, RedirectAttributes redirectAttributes) {
        List<NaverBookInfo> naverBookInfos = naverSearchService.getBookInfo(isbn).getItems();
        image = naverBookInfos.get(0).getImage();
        BookFormDTO bookFormDTO = BookFormDTO.builder()
                .title(naverBookInfos.get(0).getTitle())
                .author(naverBookInfos.get(0).getAuthor())
                .image(image)
                .isbn(isbn)
                .page(getAladinInfo(isbn)).build();

        redirectAttributes.addFlashAttribute("books", bookFormDTO);
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
