package com.kolade.demo_spring_oauth2.user;

import com.kolade.demo_spring_oauth2.util.Constants;
import lombok.RequiredArgsConstructor;
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

}
