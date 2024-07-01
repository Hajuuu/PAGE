package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.SearchUserDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.api.NaverBookDTO;
import com.Hajuuu.page.api.NaverBookInfo;
import com.Hajuuu.page.api.NaverSearchService;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final NaverSearchService naverSearchService;
    private final UserService userService;

    @GetMapping("/search")
    public String searchTitle(User loginUser, Model model) {
        model.addAttribute(new NaverBookInfo());
        return "/search/naverBook";
    }

    @PostMapping("/search")
    public String searchBook(User loginUser, @ModelAttribute("title") String title,
                             Model model) {
        if (titleInvalid(title)) {
            return "/search/naverBook";
        }
        NaverBookDTO bookInfo = naverSearchService.getBookInfo(title);

        List<NaverBookInfo> items = bookInfo.getItems();
        model.addAttribute("items", items);
        model.addAttribute("title", title);
        return "/search/naverBook";
    }

    private boolean titleInvalid(String title) {
        return title == null || title.isBlank() || title.isEmpty();
    }

    @GetMapping("/search/user")
    public String searchForm(User loginUser, Model model) {
        model.addAttribute("user", new UserDTO());
        return "/search/follow";
    }

    @PostMapping("/search/user")
    public String searchUser(User loginUser, @ModelAttribute("loginId") String loginId,
                             Model model) {

        if (titleInvalid(loginId)) {
            return "/search/follow";
        }
        List<Integer> followingList = loginUser.getFollowingList();
        List<SearchUserDTO> users = userService.search(loginId);
        for (SearchUserDTO user : users) {
            if (followingList.contains(user.getId())) {
                user.setCheck(true);
            }

        }

        model.addAttribute("loginId", loginId);
        model.addAttribute("users", users);
        return "/search/follow";
    }

    @PostMapping("/search/follow/{userId}")
    public String follow(User loginUser, @PathVariable("userId") int id,
                         RedirectAttributes redirectAttributes) {

        userService.addFollowing(loginUser, id);
        List<Integer> followingList = loginUser.getFollowingList();
        redirectAttributes.addFlashAttribute("users", followingList);
        return "redirect:/mypage";
    }

    @PostMapping("/search/cancel/{userId}")
    public String cancel(User loginUser, @PathVariable("userId") int id, Model model) {
        loginUser.cancelFollowing(id);
        Optional<User> user = userService.findOne(id);
        user.get().cancelFollower(loginUser.getId());
        return "cancel";
    }
}
