package com.kolade.demo_spring_oauth2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    public Integer getUserId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isAccountExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEmailVerified();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    //email is username
    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
