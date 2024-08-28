package com.kolade.demo_spring_oauth2.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {
    ADMIN_DEFAULT("admin:default"),
    USER_DEFAULT("user:default");

    private final String permission;
}

