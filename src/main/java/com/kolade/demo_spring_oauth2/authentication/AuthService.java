package com.kolade.demo_spring_oauth2.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolade.demo_spring_oauth2.authentication.token.Token;
import com.kolade.demo_spring_oauth2.authentication.token.TokenRepository;
import com.kolade.demo_spring_oauth2.user.Role;
import com.kolade.demo_spring_oauth2.user.User;
import com.kolade.demo_spring_oauth2.user.UserPrincipal;
import com.kolade.demo_spring_oauth2.user.UserService;
import com.kolade.demo_spring_oauth2.user.dto.UserMapper;
import com.kolade.demo_spring_oauth2.util.Constants;
import com.kolade.demo_spring_oauth2.util.HttpResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;




    public String doPasswordsMatch(String p1, String p2) {
        if (!p1.equals(p2)) {
            throw new RuntimeException("Passwords do not match");
        } else return p2;
    }

    private void validateNewUser(String email) {
        if (userService.userExists(email)) {
            throw new EntityExistsException(
                    String.format("User with email %s already exists", email)
            );
        }
    }


    public RegisterResponse register(RegisterRequest request) {
        validateNewUser(request.getEmail());
        var password = doPasswordsMatch(request.getPassword(), request.getConfirmPassword());

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .authProvider(AuthProvider.LOCAL)
                .createdAt(LocalDateTime.now())
                .isBlocked(false)
                .isAccountExpired(false)
                .isEmailVerified(true)
                .profileName(request.getProfileName())
                .passwordLastChangedDate(LocalDate.now())
                .build();

        var savedUser = userService.saveUser(user);
        var userDto = UserMapper.mapUserToUserAuthDto(savedUser);

        var userPrincipal = new UserPrincipal(user);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);

        var newToken = Token.builder()
                .userId(user.getId())
                .token(refreshToken)
                .isExpired(false)
                .isRevoked(false)
                .build();

        tokenRepository.save(newToken);

        var httpResponse = HttpResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .httpStatusCode(HttpStatus.CREATED.value())
                .reason(HttpStatus.CREATED.getReasonPhrase())
                .message(Constants.REGISTERED_MESSAGE)
                .build();

        return RegisterResponse.builder()
                .httpResponse(httpResponse)
                .user(userDto)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userService.getUserByEmail(request.getEmail());

        tokenRepository.findByUserId(user.getId()).ifPresent(tokenRepository::delete);

        var userPrincipal = new UserPrincipal(user);

        var accessToken = jwtService.generateAccessToken(userPrincipal);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);



        saveUserRefreshToken(user, refreshToken);


        var response = HttpResponse.builder()
                .httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .reason(HttpStatus.OK.getReasonPhrase())
                .message(Constants.AUTHENTICATED_MESSAGE)
                .build();

        var userDto = UserMapper.mapUserToUserAuthDto(user);


        return AuthResponse.builder()
                .accessToken(accessToken)
                .httpResponse(response)
                .user(userDto)
                .build();


    }

    private void saveUserRefreshToken(User user, String token) {

        var newTokenEntity = Token.builder()
                .userId(user.getId())
                .token(token)
                .tokenType(OAuth2AccessToken.TokenType.BEARER.getValue())
                .isExpired(false)
                .isRevoked(false)
                .build();

        tokenRepository.save(newTokenEntity);
    }


    public void refreshAccessToken (HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken;
        final String userEmail;
        if (authHeader == null || authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            return;
        }

        accessToken = authHeader.substring(Constants.TOKEN_PREFIX.length());
        userEmail = jwtService.extractUsername(accessToken);

        if (userEmail != null) {
            var user = this.userService.getUserByEmail(userEmail);
            var userPrincipal = new UserPrincipal(user);
            var refreshToken = tokenRepository.findByUserId(user.getId()).orElseThrow(() -> new EntityNotFoundException("Unknown user or token"));
            if (jwtService.isTokenValid(refreshToken.getToken(), userPrincipal)) {
                var newAccessToken = jwtService.generateAccessToken(userPrincipal);

                var authResponse = AuthResponse.builder()
                        .accessToken(newAccessToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }


        }


    }


}
