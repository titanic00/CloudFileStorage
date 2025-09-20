package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.dto.UserDTO;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthorizationController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/sign-up")
    public UserDTO signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("/sign-in")
    public UserDTO signIn(@RequestBody User user, HttpServletRequest httpRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        HttpSession session = httpRequest.getSession(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return userService.signIn(user);
    }

    @PostMapping("/sign-out")
    public void logOut() {}
}
