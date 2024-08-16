package com.Hajuuu.page.DTO;

import com.Hajuuu.page.api.BookState;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchBookDTO {

    private int id;
    @NotBlank
    private String title;

    private String image;

    @NotNull
    @Min(1)
    private Long page;

    @NotBlank
    private String author;

    @NotBlank
    private String isbn;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookState bookState;

    @QueryProjection
    public SearchBookDTO(int id, String title, String image, Long page, String author, String isbn,
                         BookState bookState) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.page = page;
        this.author = author;
        this.isbn = isbn;
        this.bookState = bookState;
    }
}
