package com.kolade.demo_spring_oauth2.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kolade.demo_spring_oauth2.user.dto.UserAuthDto;
import com.kolade.demo_spring_oauth2.util.HttpResponse;
import lombok.*;
import org.springframework.lang.Nullable;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @Nullable
    private HttpResponse httpResponse;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private UserAuthDto user;
}
