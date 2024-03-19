package com.Hajuuu.page.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NaverSearchServiceTest {

    @Autowired
    NaverSearchService naverSearchService;

    @Test
    void checkConnect() {
        String title = "물고기는 존재하지 않는다.";
        naverSearchService.getBookInfo(title);
    }

}