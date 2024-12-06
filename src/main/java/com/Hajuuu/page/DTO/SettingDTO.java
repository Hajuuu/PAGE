package com.Hajuuu.page.DTO;

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
    private String checkPassword;
    private boolean check;
}
