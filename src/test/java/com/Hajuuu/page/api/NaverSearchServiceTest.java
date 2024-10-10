package com.Hajuuu.page.api;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.Hajuuu.page.controller.SearchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class NaverSearchServiceTest {

    @InjectMocks
    private SearchController searchController;
    @Mock
    private NaverSearchService naverSearchService;

    @Mock
    private NaverBookDTO bookInfo;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(naverSearchService).build();
    }

    @DisplayName("api 연결 성공")
    @Test
    void checkConnect() throws Exception {
        //given
        String title = "물고기는 존재하지 않는다";

        doReturn(bookInfo).when(naverSearchService).getBookInfo(title);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(title)
        );

        resultActions.andExpect((ResultMatcher) jsonPath(bookInfo.getItems().get(0).getTitle()))
                .equals("물고기는 존재하지 않는다");
    }

}