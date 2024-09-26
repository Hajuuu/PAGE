package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.FollowDTO;
import com.Hajuuu.page.DTO.SettingDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.LoginForm;
import com.Hajuuu.page.domain.UploadFile;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.FileStore;
import com.Hajuuu.page.service.LoginService;
import com.Hajuuu.page.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LoginService loginService;
    private final UserService userService;
    private final FileStore fileStore;

    @GetMapping("/")
    public String homeLogin(Model model) {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();

        log.info(loginId);
        User loginUser = userService.findByLoginId(loginId);

        if (loginUser != null) {
            log.info(loginUser.toString());
            model.addAttribute("user", loginUser);
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

    @GetMapping("/mypage/follow")
    public String myfollow(@ModelAttribute("users") List<Integer> users, Model model) {
        List<FollowDTO> followList = new ArrayList<>();
        for (int i : users) {
            Optional<User> user = userService.findOne(i);
            followList.add(new FollowDTO(user.get().getId(), user.get().getLoginId()));
        }
        model.addAttribute("users", followList);
        return "/my/myfollow";
    }

    @GetMapping("/mypage")
    public String mypage(@RequestParam(value = "users", required = false) List<Integer> users, Model model) {
        List<FollowDTO> followList = new ArrayList<>();
        if (users == null) {
            model.addAttribute("users", followList);
            return "my/mypage";
        }
        for (int i : users) {
            Optional<User> user = userService.findOne(i);
            followList.add(new FollowDTO(user.get().getId(), user.get().getLoginId()));
        }
        model.addAttribute("users", followList);
        return "my/mypage";
    }

    @GetMapping("/setting")
    public String setting(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();

        User findUser = userService.findByLoginId(loginId);
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setLoginId(findUser.getLoginId());
        settingDTO.setEmail(findUser.getEmail());
        settingDTO.setPassword(findUser.getPassword());
        settingDTO.setImage(findUser.getImage());
        model.addAttribute("user", settingDTO);
        return "my/setting";
    }

    @PostMapping("/setting/fileupload")
    @Transactional
    public String fileUpload(@ModelAttribute SettingDTO settingDTO, RedirectAttributes redirectAttributes)
            throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();

        UploadFile uploadFile = fileStore.storeFile(settingDTO.getImageFile());

        String filename_url = uploadFile.getUploadName();
        String message = uploadFile.getUploadName() + " 파일이 저장되었습니다.";

        User findUser = userService.findByLoginId(loginId);
        findUser.updateProfile(fileStore.getFullPath(uploadFile.getUploadName()));
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("filename_url", filename_url);

        return "redirect:/";
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
