package com.Hajuuu.page.api;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchBookDTO {

    private String title;
    private String image;
    private Long page;
    private String author;
    private String isbn;
    @Enumerated(EnumType.STRING)
    private BookState bookState;

}
