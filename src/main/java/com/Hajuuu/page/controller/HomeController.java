package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.FollowDTO;
import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.ReadingStatsDTO;
import com.Hajuuu.page.DTO.SettingDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.LoginForm;
import com.Hajuuu.page.domain.UploadFile;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.security.CustomUserDetails;
import com.Hajuuu.page.service.FileStore;
import com.Hajuuu.page.service.ReadingStatsService;
import com.Hajuuu.page.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final FileStore fileStore;
    private final ReadingStatsService readingStatsService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            User loginUser = userService.findByLoginId(userDetails.getUsername()); // 또는 getLoginId() 메서드

            List<PostDTO> posts = userService.findFollowingUsersPost(loginUser);

            if (posts.isEmpty()) {
                model.addAttribute("message", "아직 팔로우 중인 사용자가 없습니다. 팔로우 해보세요!");
            } else {
                model.addAttribute("posts", posts);
            }
            model.addAttribute("readingStats", readingStatsService.getStats(loginUser.getId()));
        } else {
            model.addAttribute("readingStats",
                    new ReadingStatsDTO(0, 0, 0));
        }
        return "home"; // home.html
    }


    @GetMapping("/join")
    public String joinForm(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
        model.addAttribute("userDTO", userDTO);

        return "/member/joinForm";
    }

    @PostMapping("/join")
    public String saveUser(@Validated @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult) {

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
            return "/mypage";
        }
        for (int i : users) {
            Optional<User> user = userService.findOne(i);
            followList.add(new FollowDTO(user.get().getId(), user.get().getLoginId()));
        }
        model.addAttribute("users", followList);
        return "/mypage";
    }

    @GetMapping("/setting")
    public String setting(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User findUser = userService.findByLoginId(userDetails.getUsername());
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setLoginId(findUser.getLoginId());
        settingDTO.setName(findUser.getName());
        settingDTO.setEmail(findUser.getEmail());
        settingDTO.setPassword(findUser.getPassword());
        settingDTO.setImage(findUser.getImage());
        model.addAttribute("user", settingDTO);
        return "/my/setting";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @PostMapping("/setting/fileupload")
    @Transactional
    public String fileUpload(@ModelAttribute SettingDTO settingDTO)
            throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();

        UploadFile uploadFile = fileStore.storeFile(settingDTO.getImageFile());

        String message = uploadFile.getUploadName() + " 파일이 저장되었습니다.";

        //"D:\page\image\0b0cb23d-9c54-447f-8c5a-b7237d3e77fa.jpg"
        User findUser = userService.findByLoginId(loginId);
        findUser.updateProfile(uploadFile.getStoreName());

        String fileUrl = fileStore.getFullPath(findUser.getImage());

        return "redirect:/setting";
    }

    @PostMapping("/setting/filedelete")
    @Transactional
    public String fileDelete(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @ModelAttribute SettingDTO settingDTO)
            throws IOException {
        User findUser = userService.findByLoginId(userDetails.getUsername());

        String fileUrl = fileStore.getFullPath(findUser.getImage());

        Path filePath = Paths.get("static", fileUrl);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            findUser.deleteProfile();
        }
        return "redirect:/setting";
    }

    @PostMapping("/setting/updateName")
    @Transactional
    public String updateName(@ModelAttribute("user") SettingDTO settingDTO) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();

        User findUser = userService.findByLoginId(loginId);
        findUser.updateName(settingDTO.getName());

        return "redirect:/setting";
    }

    @PostMapping("/setting/checkPassword")
    public String checkPassword(@ModelAttribute("user") SettingDTO settingDTO, Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();

        User findUser = userService.findByLoginId(loginId);
        settingDTO.setLoginId(findUser.getLoginId());
        settingDTO.setName(findUser.getName());
        settingDTO.setEmail(findUser.getEmail());
        settingDTO.setPassword(findUser.getPassword());
        settingDTO.setImage(findUser.getImage());
        model.addAttribute("user", settingDTO);
        return "/my/setting";
    }

    @PostMapping("/setting/changePassword")
    public String changePassword(@ModelAttribute("user") SettingDTO settingDTO) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();
        settingDTO.setLoginId(loginId);
        userService.changePassword(settingDTO);
        return "redirect:/setting";
    }

    @PostMapping("/setting/updateEmail")
    @Transactional
    public String updateEmail(@ModelAttribute("user") SettingDTO settingDTO) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String loginId = authentication.getName();
        User findUser = userService.findByLoginId(loginId);
        findUser.updateEmail(settingDTO.getEmail());
        return "redirect:/setting";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form) {
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
