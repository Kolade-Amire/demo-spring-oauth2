package com.kolade.demo_spring_oauth2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {

    private final User user;

    private Map<String, Object> attributes;



    public Integer getUserId() {
        return this.user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.user.isAccountExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEmailVerified();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    //email is username
    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {

        UserPrincipal userPrincipal = new UserPrincipal(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;

    }

    private void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.user.getName();
    }
}
