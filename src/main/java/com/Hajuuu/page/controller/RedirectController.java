package com.Hajuuu.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
    public String redirectToRoot() {
        return "redirect:/"; // '/' 경로로 리다이렉트
    }
}
