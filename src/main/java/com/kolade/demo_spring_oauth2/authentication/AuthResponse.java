package com.kolade.demo_spring_oauth2.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kolade.demo_spring_oauth2.user.dto.UserAuthDto;
import com.kolade.demo_spring_oauth2.util.HttpResponse;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {


    private HttpResponse httpResponse;
    @JsonProperty("access_token")
    private String accessToken;
    private UserAuthDto user;
}
