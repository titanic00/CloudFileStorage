package com.titanic00.cloudfilestorage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    @PostMapping("/sign-up")
    public void signUp() {
        return;
    }

    @PostMapping("/sign-in")
    public void signIn() {
        return;
    }

    @PostMapping("/sign-out")
    public void logOut() {
        return;
    }
}
