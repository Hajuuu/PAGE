package com.Hajuuu.page.security;

import java.util.Map;


public class NaverUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public NaverUserDetails(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }


}
