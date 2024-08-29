package com.kolade.demo_spring_oauth2.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface UserService {

    User getUserByEmail(String email) throws UsernameNotFoundException;

    User saveUser(User user);

    boolean userExists(String email);
}
