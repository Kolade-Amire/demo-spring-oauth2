package com.kolade.demo_spring_oauth2.user.dto;

import com.kolade.demo_spring_oauth2.user.User;

public class UserMapper {

    public static UserAuthDto mapUserToUserAuthDto(User user) {
        return UserAuthDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileName(user.getProfileName())
                .role(user.getRole())
                .authProvider(user.getAuthProvider().toString())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
