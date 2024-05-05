package com.Hajuuu.page.DTO;

import com.Hajuuu.page.api.BookState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchBookDTO {

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

}
