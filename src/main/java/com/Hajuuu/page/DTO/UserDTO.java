package com.Hajuuu.page.DTO;

import com.Hajuuu.page.domain.Role;
import com.Hajuuu.page.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
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

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .followerList(new ArrayList<>())
                .followingList(new ArrayList<>())
                .image("/assets/images/logos/user-regular.svg")
                .role(Role.USER)
                .build();
    }
}
