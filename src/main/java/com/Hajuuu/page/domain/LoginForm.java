package com.Hajuuu.page.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginForm {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
