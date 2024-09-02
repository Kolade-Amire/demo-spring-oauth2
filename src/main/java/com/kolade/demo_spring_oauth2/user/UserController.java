package com.kolade.demo_spring_oauth2.user;

import com.kolade.demo_spring_oauth2.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.BASE_URL + "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    public String hello(@RequestParam String email) {
        var user = userService.getUserByEmail(email);
        return "Hello " + user.getProfileName();
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
}
