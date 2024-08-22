package com.Hajuuu.page.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "naver")
public class NaverProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;

}
