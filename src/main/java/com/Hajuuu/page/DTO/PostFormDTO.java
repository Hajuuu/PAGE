package com.Hajuuu.page.DTO;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostFormDTO {

    private String content;
    private LocalDateTime date;
    private Long bookId;
    private Long page;

    @QueryProjection
    public PostFormDTO(String content, LocalDateTime date, Long bookId, Long page) {
        this.content = content;
        this.date = date;
        this.bookId = bookId;
        this.page = page;
    }
}
