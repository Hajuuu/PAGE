package com.Hajuuu.page.api;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    @GetMapping("/search/{title}")
    public NaverBookDTO getNaver(@PathVariable("title") String title, Model model) throws IOException {
        NaverBookDTO naverBooks = NaverSearchService.get(title);
        model.addAttribute(naverBooks);
        return naverBooks;
    }

    @GetMapping("/searchDetail/{isbn13}")
    public AladinBookDTO getAladin(@PathVariable("isbn13") String isbn13, Model model) throws IOException {
        AladinBookDTO aladinBookDTO = AladinSearchService.get(isbn13);
        return aladinBookDTO;
    }
}
