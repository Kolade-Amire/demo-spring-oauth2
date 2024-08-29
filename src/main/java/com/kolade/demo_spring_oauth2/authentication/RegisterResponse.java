package com.kolade.demo_spring_oauth2.authentication;

import com.kolade.demo_spring_oauth2.user.dto.UserAuthDto;
import com.kolade.demo_spring_oauth2.util.HttpResponse;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private HttpResponse httpResponse;
    private UserAuthDto user;
}
