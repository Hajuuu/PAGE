package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.FollowDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.LoginForm;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.LoginService;
import com.Hajuuu.page.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LoginService loginService;
    private final UserService userService;

    @GetMapping("/")
    public String homeLogin(User loginUser,
                            Model model) {

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        log.info("시큐리티 " + SecurityContextHolder.getContext().getAuthentication());
        User findUser = userService.findByLoginId(loginId);
        if (findUser != null) {
            model.addAttribute("user", findUser);
            model.addAttribute("name", loginId);
            return "loginHome";
        }
        model.addAttribute("name", loginId);
        return "home";
    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("user") UserDTO userDTO, Model model) {
        model.addAttribute("user", userDTO);

        return "/member/joinForm";
    }

    @PostMapping("/join")
    public String saveUser(@Validated @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/member/joinForm";
        }
        userService.securityJoin(userDTO);
        return "redirect:/";
    }

    @GetMapping("/login/naverLogin")
    public String loginNaver() {
        return "redirect:/oauth2/authorization/naver";
    }

    @GetMapping("/my/myfollow")
    public String myfollow(@ModelAttribute("users") List<Integer> users, Model model) {
        List<FollowDTO> followList = new ArrayList<>();
        for (int i : users) {
            Optional<User> user = userService.findOne(i);
            followList.add(new FollowDTO(user.get().getId(), user.get().getLoginId()));
        }
        model.addAttribute("users", followList);
        return "/my/myfollow";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "/member/loginForm";
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
