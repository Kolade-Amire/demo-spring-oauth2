package com.kolade.demo_spring_oauth2.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kolade.demo_spring_oauth2.user.Permissions.ADMIN_DEFAULT;
import static com.kolade.demo_spring_oauth2.user.Permissions.USER_DEFAULT;


@Getter
@RequiredArgsConstructor
public enum Role {

    USER(Set.of(
            USER_DEFAULT
    )
    ),
    ADMIN(
            Set.of(
                    ADMIN_DEFAULT
            )
    );
    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;

    }

}

