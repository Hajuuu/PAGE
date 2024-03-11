package com.Hajuuu.page.service;

import com.Hajuuu.page.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    User user1 = new User();
    User user2 = new User();

    void init() {
        user1.createUser("abc123", "asdf1234");
        user2.createUser("abc345", "asdf12345");

    }

    @Test
    void 로그인_실패() {
        //given
        init();
        userService.join(user1);
        userService.join(user2);

        //when
        User loginUser = loginService.login("abc123", "asdf");

        //then
        Assertions.assertNull(loginUser);
    }


    @Test
    void 로그인_성공() {
        //given
        init();
        userService.join(user1);
        userService.join(user2);

        //when
        User loginUser = loginService.login("abc345", "asdf12345");

        //then
        org.assertj.core.api.Assertions.assertThat(loginUser.getId()).isEqualTo(user2.getId());
    }
}