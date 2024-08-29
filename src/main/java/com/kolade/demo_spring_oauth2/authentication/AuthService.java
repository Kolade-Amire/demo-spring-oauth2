package com.kolade.demo_spring_oauth2.authentication;

import com.kolade.demo_spring_oauth2.authentication.token.TokenRepository;
import com.kolade.demo_spring_oauth2.security.LoginAttemptService;
import com.kolade.demo_spring_oauth2.user.Role;
import com.kolade.demo_spring_oauth2.user.User;
import com.kolade.demo_spring_oauth2.user.UserPrincipal;
import com.kolade.demo_spring_oauth2.user.UserService;
import com.kolade.demo_spring_oauth2.util.Constants;
import com.kolade.demo_spring_oauth2.util.HttpResponse;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;


    public String doPasswordsMatch(String p1, String p2) {
        if (!p1.equals(p2)) {
            throw new RuntimeException("Passwords do not match");
        } else return p2;
    }

    private void validateNewUser(String email){
        if (userService.userExists(email)){
            throw new EntityExistsException(
                    String.format("User with email %s already exists", email)
            );
        }
    }


    public AuthResponse register(RegisterRequest request) {
        validateNewUser(request.getEmail());
        doPasswordsMatch(request.getPassword(), request.getConfirmPassword());
        var password = doPasswordsMatch(request.getPassword(), request.getConfirmPassword());


        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .authProvider("local")
                .createdAt(LocalDateTime.now())
                .isBlocked(false)
                .isAccountExpired(false)
                .isEmailVerified(false)
                .profileName(request.getProfileName())
                .passwordLastChangedDate(LocalDate.now())
                .build();

        var savedUser = userService.saveUser(user);
        var userPrincipal = new UserPrincipal(savedUser);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);

        var httpResponse = HttpResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .httpStatusCode(HttpStatus.CREATED.value())
                .reason(HttpStatus.CREATED.getReasonPhrase())
                .message(Constants.REGISTERED_MESSAGE)
                .build();

        return AuthResponse.builder()
                .accessToken("Account created. You can now login.")
                .refreshToken(refreshToken)
                .httpResponse(httpResponse)
                .build();
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.getUserByEmail(username);
//        validateLoginAttempt(user)
//        userService.saveUser(user);
        return new UserPrincipal(user);
    }



}
