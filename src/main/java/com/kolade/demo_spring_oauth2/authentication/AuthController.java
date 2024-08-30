package com.kolade.demo_spring_oauth2.authentication;

import com.kolade.demo_spring_oauth2.security.LogoutService;
import com.kolade.demo_spring_oauth2.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping(Constants.BASE_URL + "/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final LogoutService logoutService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshAccessToken(request, response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }
}
