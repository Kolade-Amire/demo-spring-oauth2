package com.kolade.demo_spring_oauth2.security;

import com.kolade.demo_spring_oauth2.authentication.JwtService;
import com.kolade.demo_spring_oauth2.authentication.token.TokenRepository;
import com.kolade.demo_spring_oauth2.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().contains(Constants.BASE_URL + "/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userEmail;

        if (authHeader == null || authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        token = authHeader.substring(Constants.TOKEN_PREFIX.length());
        userEmail = jwtService.extractUsername(token);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userPrincipal = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(token, userPrincipal) && !isTokenExpired(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);


    }

    protected boolean isTokenExpired(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> t.isExpired() && t.isRevoked())
                .orElse(false);
    }
}
