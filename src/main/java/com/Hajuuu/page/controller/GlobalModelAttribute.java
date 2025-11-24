package com.Hajuuu.page.controller;

import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.security.CustomUserDetails;
import com.Hajuuu.page.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {
    private final UserService userService;

    public GlobalModelAttribute(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User user(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userService.findByLoginId(userDetails.getUsername());
    }
}
