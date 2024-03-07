package com.Hajuuu.page.controller;

import com.Hajuuu.page.api.NaverBookDTO;
import com.Hajuuu.page.api.NaverBookInfo;
import com.Hajuuu.page.api.NaverSearchService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final NaverSearchService naverSearchService;

    @GetMapping("/search")
    public String searchTitle(Model model)
            throws IOException {
        model.addAttribute(new NaverBookInfo());
        return "/search/naverBook";
    }

    @PostMapping("/search")
    public String searchBook(@ModelAttribute("title") String title,
                             @ModelAttribute("items") NaverBookInfo naverBookInfo,
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


}
