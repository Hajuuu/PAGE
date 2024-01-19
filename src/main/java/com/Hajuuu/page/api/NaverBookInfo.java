package com.Hajuuu.page.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NaverBookInfo {

    private String title;
    private String link;
    private String image;
    private String author;
    private String discount;
    private String publisher;
    private String isbn;
    private String description;
    private String pubdate;
}
