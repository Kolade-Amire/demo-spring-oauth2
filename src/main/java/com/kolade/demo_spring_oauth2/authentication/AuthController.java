package com.kolade.demo_spring_oauth2.authentication;

import com.kolade.demo_spring_oauth2.security.LogoutService;
import com.kolade.demo_spring_oauth2.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping(Constants.BASE_URL + "/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final LogoutService logoutService;
    private final ClientRegistrationRepository clientRegistrationRepository;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Login</title>
                </head>
                <body>
                    <h2>Login</h2>
                    <form id="loginForm">
                        <label for="email">Email:</label>
                        <input type="text" id="email" name="email" required><br><br>
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required><br><br>
                        <button type="button" onclick="submitLogin()">Login</button>
                    </form>
                    <hr>
                    <button id="googleSignInButton">Sign in with Google</button>
                    <script>
                        function submitLogin() {
                            const email = document.getElementById('email').value;
                            const password = document.getElementById('password').value;

                            const requestBody = {
                                email: email,
                                password: password
                            };

                            fetch('http://localhost:8080/api/v1/auth/login', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(requestBody)
                            })
                            .then(response => response.json())
                            .then(data => {
                                // Handle successful authentication
                                console.log('Success:', data);
                                // Redirect to a new page or show user info
                            })
                            .catch((error) => {
                                console.error('Error:', error);
                                // Handle authentication error
                            });
                        }

                        document.getElementById('googleSignInButton').onclick = function() {
                            window.location.href = '/oauth2/authorization/google';
                        };
                    </script>
                </body>
                </html>
                """;

        return ResponseEntity.ok().body(html);
    }
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Login</title>
                </head>
                
                <body>
                    <h1>Successfully logged in!</h1>
                </body>
                </html>
                
                """;

        return ResponseEntity.ok().body(html);
    }

    @PostMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
       service.refreshAccessToken(request, response);
       response.getOutputStream();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }
}
