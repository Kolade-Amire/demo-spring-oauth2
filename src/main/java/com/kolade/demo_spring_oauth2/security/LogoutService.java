package com.kolade.demo_spring_oauth2.security;

import com.kolade.demo_spring_oauth2.authentication.token.TokenRepository;
import com.kolade.demo_spring_oauth2.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        if (authHeader == null || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            return;
        }
        token = authHeader.substring(Constants.TOKEN_PREFIX.length());
        var savedToken = tokenRepository.findByToken(token)
                .orElse(null);
        if (savedToken != null) {
            savedToken.setExpired(true);
            savedToken.setRevoked(true);
            tokenRepository.delete(savedToken);
            SecurityContextHolder.clearContext();
        }

    }
}
