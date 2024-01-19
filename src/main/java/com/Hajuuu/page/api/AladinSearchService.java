package com.Hajuuu.page.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public static AladinBookDTO get(String isbn13) throws IOException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiURL);
        factory.setEncodingMode(EncodingMode.NONE);

        WebClient client = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(apiURL)
                .build();


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

}
