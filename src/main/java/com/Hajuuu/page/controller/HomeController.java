package com.Hajuuu.page.controller;

import com.Hajuuu.page.domain.LoginForm;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.LoginService;
import com.Hajuuu.page.service.UserService;
import com.Hajuuu.page.web.argumentresolver.Login;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LoginService loginService;
    private final UserService userService;

    @GetMapping("/")
    public String homeLogin(@Login User loginUser,
                            Model model) {
        if (loginUser == null) {
            return "home";
        }
        model.addAttribute("user", loginUser);
        return "loginHome";
    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "/member/joinForm";
    }

    @PostMapping("/join")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.join(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "/member/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form,
                        @RequestParam(defaultValue = "/", name = "redirectURL") String redirectURL,
                        HttpServletRequest request) {
        User loginMember = loginService.login(form.getLoginId(), form.getPassword());

        log.info("login? {}", loginMember);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginMember);

        log.info(redirectURL);
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
