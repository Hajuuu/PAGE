package com.Hajuuu.page.DTO;

import com.Hajuuu.page.domain.Role;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {

    private int id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String image;
    private Role role;
    private String provider;
    private String providerId;
    private List<Integer> followers;
    private List<Integer> followings;

    @QueryProjection
    public UserResponseDTO(int id, String loginId, String password, String name, String email, String image, Role role,
                           String provider, String providerId, List<Integer> followers, List<Integer> followings) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.image = image;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.followers = followers;
        this.followings = followings;
    }
}
