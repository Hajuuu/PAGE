package com.Hajuuu.page.api;

import java.io.IOException;
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
public class AladinSearchService {

    private static String TTBKey = "ttbhhjy17131732001";
    private static final String apiURL = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";

    public static AladinBookDTO getBookInfo(String isbn13) throws IOException {
        WebClient client = webClient();
        Mono<AladinBookDTO> response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("ttbkey", URLEncoder.encode(TTBKey))
                        .queryParam("itemIdType", "ISBN13")
                        .queryParam("ItemId", isbn13)
                        .queryParam("output", "js")
                        .queryParam("Version", "20131101")
                        .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AladinBookDTO.class);

        AladinBookDTO aladinBookDTO = response.block();

        return aladinBookDTO;
    }

    private static WebClient webClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiURL);
        factory.setEncodingMode(EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(apiURL)
                .build();
    }
}
