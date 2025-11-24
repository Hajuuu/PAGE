package com.Hajuuu.page.controller;

import com.Hajuuu.page.DTO.SearchUserDTO;
import com.Hajuuu.page.DTO.UserResponseDTO;
import com.Hajuuu.page.api.NaverBookDTO;
import com.Hajuuu.page.api.NaverBookInfo;
import com.Hajuuu.page.api.NaverSearchService;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.security.CustomUserDetails;
import com.Hajuuu.page.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final NaverSearchService naverSearchService;
    private final UserService userService;

    @GetMapping("/search")
    public String searchTitle(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("user", null);
            return "home";
        }
        String loginId = userDetails.getUsername();
        User loginUser = userService.findByLoginId(loginId);
        model.addAttribute("user", loginUser);
        model.addAttribute(new NaverBookInfo());
        return "/search/naverBook";
    }

    @PostMapping("/search")
    public String searchBook(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @ModelAttribute("title") String title, Model model) {
        if (titleInvalid(title)) {
            return "/search/naverBook";
        }
        NaverBookDTO bookInfo = naverSearchService.getBookInfo(title);
        String loginId = userDetails.getUsername();
        User loginUser = userService.findByLoginId(loginId);
        model.addAttribute("user", loginUser);
        List<NaverBookInfo> items = bookInfo.getItems();
        model.addAttribute("items", items);
        model.addAttribute("title", title);
        return "/search/naverBook";
    }

    private boolean titleInvalid(String title) {
        return title == null || title.isBlank();
    }

    @GetMapping("/search/user")
    public String searchForm(Model model) {
        model.addAttribute("user", new UserResponseDTO());
        return "/search/follow";
    }

    @PostMapping("/search/user")
    public String searchUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @ModelAttribute("loginId") String searchKeyword,
                             Model model) {

        // 1. 로그인한 유저 정보는 무조건 SecurityContext에서 가져오기
        User loginUser = userService.findByLoginId(userDetails.getUsername());

        // 2. 검색 키워드 검증
        if (titleInvalid(searchKeyword)) {
            return "/search/follow";
        }

        // 3. 로그인한 유저의 팔로잉 리스트
        List<Integer> followingList = loginUser.getFollowings();

        // 4. 검색 결과 가져오기
        List<SearchUserDTO> users = userService.search(searchKeyword);

        // 5. 팔로잉 여부 체크
        for (SearchUserDTO user : users) {
            if (user.getId() == loginUser.getId()) {
                user.setCheck(true);
                continue;
            }
            user.setCheck(followingList.contains(user.getId()));
        }

        model.addAttribute("loginId", searchKeyword);
        model.addAttribute("users", users);

        return "/search/follow";
    }


    @PostMapping("/search/follow/{userId}")
    @ResponseBody
    public Map<String, Object> follow(@PathVariable("userId") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        User loginUser = userService.findByLoginId(loginId);

        userService.addFollowing(loginUser, id);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "팔로잉 완료");
        return result;
    }

    @PostMapping("/search/cancel/{userId}")
    @ResponseBody
    public Map<String, Object> cancel(@PathVariable("userId") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        User loginUser = userService.findByLoginId(loginId);

        //userService.removeFollowing(loginUser, id);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "팔로우 취소 완료");
        return result;
    }

}
