package com.Hajuuu.page.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NaverSearchService {

    private static final String clientId = "G7IrKRPfEKzpqYyM1df3";
    private static final String clientSecret = "PqdCvNfbNl";

    private static String apiURL = "https://openapi.naver.com/v1/search/book.json";

    public static NaverBookDTO get(String title) throws IOException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiURL);
        factory.setEncodingMode(EncodingMode.NONE);

        title = URLEncoder.encode(title, "UTF-8");
        WebClient client = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(apiURL)
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .build();

        String finalTitle = title;

        Mono<NaverBookDTO> response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", finalTitle)
                        .queryParam("display", 5)
                        .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(NaverBookDTO.class);
        NaverBookDTO naverBookDTO = response.block();
        return naverBookDTO;
    }

}
