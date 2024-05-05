package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.FollowDTO;
import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.LoginForm;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.LoginService;
import com.Hajuuu.page.service.UserService;
import com.Hajuuu.page.web.argumentresolver.Login;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
            model.addAttribute("loginUser", null);
            return "home";
        }
        List<PostDTO> posts = userService.findFollowingUsersPost(loginUser);
        model.addAttribute("posts", posts);
        model.addAttribute("user", loginUser);
        return "loginHome";
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
        User user = new User();
        user.createUser(userDTO.getLoginId(), userDTO.getPassword());
        userService.join(user);
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(@ModelAttribute("users") List<Integer> users, Model model) {
        List<FollowDTO> followList = new ArrayList<>();
        for (int i : users) {
            Optional<User> user = userService.findOne(i);
            followList.add(new FollowDTO(user.get().getId(), user.get().getLoginId()));
        }
        model.addAttribute("users", followList);
        return "/my/mypage";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "/member/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form,
                        @RequestParam(defaultValue = "/", name = "redirectURL") String redirectURL,
                        HttpServletRequest request) {
        Optional<User> loginMember = loginService.login(form.getLoginId(), form.getPassword());

        log.info("login? {}", loginMember);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginMember.get());

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
