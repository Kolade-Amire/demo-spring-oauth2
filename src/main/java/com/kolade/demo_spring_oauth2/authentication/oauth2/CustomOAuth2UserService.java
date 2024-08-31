package com.kolade.demo_spring_oauth2.authentication.oauth2;

import com.kolade.demo_spring_oauth2.authentication.AuthProvider;
import com.kolade.demo_spring_oauth2.user.Role;
import com.kolade.demo_spring_oauth2.user.User;
import com.kolade.demo_spring_oauth2.user.UserPrincipal;
import com.kolade.demo_spring_oauth2.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception exception) {
            throw new OAuth2AuthenticationException(exception.getMessage());
        }
    }

    public OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration()
                        .getRegistrationId(), oauth2User.getAttributes()
        );

        User user = userService.getUserByEmail(oAuth2UserInfo.getEmail());
        if(user == null) {
            user = registerUser(userRequest, oAuth2UserInfo);
        }else {
            user = updateExistingUser(user, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oauth2User.getAttributes());
    }


    public User registerUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) throws OAuth2AuthenticationException {
        var user = User.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .authProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .isEmailVerified(true)
                .build();

        return userService.saveUser(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        return  userService.saveUser(existingUser);
    }
}
