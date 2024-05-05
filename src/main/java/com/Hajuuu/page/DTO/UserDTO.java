package com.Hajuuu.page.DTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private String loginId;
    private String password;

    @QueryProjection
    public UserDTO(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
