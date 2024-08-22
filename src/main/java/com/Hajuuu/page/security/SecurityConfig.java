package com.Hajuuu.page.security;

import com.Hajuuu.page.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable());
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/logout", "/join", "/css/**", "/assets/**", "js/**",
                                "/style.css", "/login/naverLogin").permitAll()
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated());
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProcess")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/")
                        .permitAll());

        http
                .logout((auth) -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"));
        http
                .oauth2Login((auth) -> auth.loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login")
                        .permitAll());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
