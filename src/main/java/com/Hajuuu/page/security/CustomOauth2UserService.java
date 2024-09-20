package com.Hajuuu.page.security;

import com.Hajuuu.page.domain.Role;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        log.info("로그인 리퀘스트: {}", userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }
        String providerId = oAuth2UserInfo.getProviderId();
        log.info(providerId);
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();

        User findUser = userRepository.findByLoginId(loginId);
        User user;
        if (findUser == null) {
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .email(email)
                    .image("/assets/images/logos/user-regular.svg")
                    .followingList(new ArrayList<>())
                    .followerList(new ArrayList<>())
                    .provider(provider)
                    .providerId(providerId)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        } else {
            user = findUser;
        }

        return new CustomOauth2UserDetails(user, oAuth2User.getAttributes());
    }
}
