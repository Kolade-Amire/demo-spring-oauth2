package com.kolade.demo_spring_oauth2.authentication.oauth2;

public interface OAuth2UserInfo {

    String getId();
    String getName();
    String getEmail();
    String getImageUrl();
}
