package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.dto.UserDTO;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.exception.AlreadySignedIn;
import com.titanic00.cloudfilestorage.exception.IncorrectCredentials;
import com.titanic00.cloudfilestorage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        if(!userService.userExists(user)) {
            throw new IncorrectCredentials("User not found.");
        }

        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            throw new AlreadySignedIn("You are already signed in.");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            HttpSession newSession = httpRequest.getSession(true);
            SecurityContextHolder.getContext().setAuthentication(auth);
            newSession.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return userService.signIn(user);
        } catch (BadCredentialsException ex) {
            throw new IncorrectCredentials("Username or password is wrong.");
        }
    }
}
