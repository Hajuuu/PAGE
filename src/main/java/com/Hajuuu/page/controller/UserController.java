package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.PostFormDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.Post;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.BookService;
import com.Hajuuu.page.service.PostService;
import com.Hajuuu.page.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final BookService bookService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/books")
    public String findUserBooks(User loginUser,
                                Model model) {

        List<Book> books = userService.findBooks(loginUser.getEmail());

        model.addAttribute("books", books);
        model.addAttribute("user", loginUser);
        return "/my/books";
    }


    @GetMapping("/books/{bookId}/new")
    public String createPost(User loginUser, @PathVariable("bookId") int bookId, Model model) {
        Book book = bookService.findOne(bookId);
        PostFormDTO postForm = new PostFormDTO();
        postForm.setBookId(bookId);
        log.info(String.valueOf(bookId));
        model.addAttribute("posts", postForm);

        return "/my/createPostForm";
    }

    @PostMapping("/post/save")
    public String savePost(User loginUser, @ModelAttribute("post") PostFormDTO postFormDTO) {

        int bookId = postFormDTO.getBookId();
        Post post = new Post();
        Book book = bookService.findOne(bookId);
        log.info(postFormDTO.getContent());
        book.changePage(postFormDTO.getPage());
        post.createPost(book, loginUser, postFormDTO.getContent());
        postService.savePost(post);

        log.info("포스트 저장");
        return "redirect:/books/" + bookId + "/posts";
    }

    @GetMapping("/books/{bookId}/posts")
    public String allPosts(@PathVariable("bookId") int bookId, Model model) {
        List<PostFormDTO> posts = postService.findPosts(bookId);
        Book book = bookService.findOne(bookId);
        model.addAttribute("book", book);
        model.addAttribute("posts", posts);
        return "/my/posts";
    }


}
