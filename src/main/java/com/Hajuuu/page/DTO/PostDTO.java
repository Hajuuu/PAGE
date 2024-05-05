package com.Hajuuu.page.DTO;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDTO {

    String bookTitle;
    String content;
    LocalDateTime createdTime;
    String loginId;

    @QueryProjection
    public PostDTO(String bookTitle, String content, LocalDateTime createdTime, String loginId) {
        this.bookTitle = bookTitle;
        this.content = content;
        this.createdTime = createdTime;
        this.loginId = loginId;
    }
}
