package com.titanic00.cloudfilestorage.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @PostMapping("/sign-up")
    public void signUp() {};

    @PostMapping("/sign-in")
    public void signIn() {};

    @PostMapping("/sign-out")
    public void signOut() {};
}
