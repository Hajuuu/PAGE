package com.Hajuuu.page.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NaverSearchService {
    private static final String clientId = "";
    private static final String clientSecret = "";

    private static String apiURL = "https://openapi.naver.com/v1/search/book.json";

    private static WebClient webClient;

    public NaverBookDTO getBookInfo(String title) throws IOException {
        String encodedTitle = encodingTitle(title);
        webClient = createWebClient();
        Mono<NaverBookDTO> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", encodedTitle)
                        .queryParam("display", 5)
                        .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(NaverBookDTO.class);
        NaverBookDTO naverBookDTO = response.block();
        return naverBookDTO;
    }

    private WebClient createWebClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiURL);
        factory.setEncodingMode(EncodingMode.NONE);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(apiURL)
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .build();
    }

    private String encodingTitle(String title) {
        String encodedText = "";
        try {
            encodedText = URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
        return encodedText;
    }
}
