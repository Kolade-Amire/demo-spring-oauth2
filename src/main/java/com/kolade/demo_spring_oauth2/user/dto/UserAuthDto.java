package com.kolade.demo_spring_oauth2.user.dto;

import com.kolade.demo_spring_oauth2.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserAuthDto {
    private Integer id;
    private String name;
    private String email;
    private String profileName;
    private Role role;
    private String authProvider;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
