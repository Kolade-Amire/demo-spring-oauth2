package com.kolade.demo_spring_oauth2.security;

import com.kolade.demo_spring_oauth2.user.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;




    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String ipAddress = getClientIpAddress();

        if(loginAttemptService.isBlocked(ipAddress)){
            throw new LockedException("This user is blocked due to too many failed attempts");
        }

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);


        return authProvider.authenticate(authentication);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    private String getClientIpAddress() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
