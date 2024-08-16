package com.Hajuuu.page.DTO;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {

    int id;
    String loginId;
    LocalDateTime createdTime;

    @QueryProjection
    public FollowDTO(int id, String loginId) {
        this.id = id;
        this.loginId = loginId;
    }

}
