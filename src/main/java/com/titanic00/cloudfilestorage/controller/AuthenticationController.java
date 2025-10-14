package com.titanic00.cloudfilestorage.controller;

import com.titanic00.cloudfilestorage.dto.UserDTO;
import com.titanic00.cloudfilestorage.dto.request.AuthorizationRequest;
import com.titanic00.cloudfilestorage.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid AuthorizationRequest authorizationRequest) {
        UserDTO registered = userService.signUp(authorizationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    };

    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> signIn(@RequestBody AuthorizationRequest authorizationRequest) {
        UserDTO signedIn = userService.signIn(authorizationRequest);

        return ResponseEntity.status(HttpStatus.OK).body(signedIn);
    };

    @PostMapping("/sign-out")
    public void signOut() {};
}
