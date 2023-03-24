package com.hescha.minijira.service;

import com.hescha.minijira.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    private static User defaultUser;

    static {
        defaultUser = new User();
        defaultUser.setFirstname("test");
        defaultUser.setLastname("test");
        defaultUser.setUsername("test");
        defaultUser.setId(99L);
    }

    public User getLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return defaultUser;
        return userService.findByUsername(auth.getName());
    }
}
