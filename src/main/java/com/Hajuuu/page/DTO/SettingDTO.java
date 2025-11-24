package com.Hajuuu.page.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class SettingDTO {

    private String loginId;
    private String password;
    private String name;
    private MultipartFile imageFile;
    private String image;
    private String email;
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String checkPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, message = "새 비밀번호를 입력해주세요.")
    private String changePassword;

    @NotBlank(message = "새 비밀번호 확인을 입력해주세요.")
    private String checkChangePassword;
}
