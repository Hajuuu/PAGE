package com.Hajuuu.page.controller;

import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.BookService;
import com.Hajuuu.page.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final BookService bookService;
    private final UserService userService;


    @GetMapping("/users/books")
    public String findUserBooks(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                Model model) {

        if (loginUser == null) {
            return "redirect:/login";
        }
        List<Book> books = userService.findBooks(loginUser.getLoginId());

        model.addAttribute("books", books);
        model.addAttribute("user", loginUser);
        return "/my/books";
    }


    @PostMapping("/users/books/{id}/posts")
    public String addPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", new Book());
        return "/my/createPostForm";
    }


}
