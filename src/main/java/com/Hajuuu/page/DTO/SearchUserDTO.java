package com.Hajuuu.page.DTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchUserDTO {

    private int id;
    private String loginId;
    private boolean check = false;

    @QueryProjection
    public SearchUserDTO(int id, String loginId) {
        this.id = id;
        this.loginId = loginId;
    }
}
