package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.dto.UserDTO;
import com.titanic00.cloudfilestorage.dto.request.AuthorizationRequest;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.exception.AlreadyExistsException;
import com.titanic00.cloudfilestorage.exception.ValidationErrorException;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResourceService resourceService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final SecurityContextRepository securityContextRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, ResourceService resourceService,
                       AuthenticationManager authenticationManager,
                       SecurityContextRepository securityContextRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resourceService = resourceService;
        this.authenticationManager = authenticationManager;
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        this.securityContextRepository = securityContextRepository;
    }


    public UserDTO signUp(AuthorizationRequest authorizationRequest) throws Exception {
        if (usernameExists(authorizationRequest.getUsername())) {
            throw new AlreadyExistsException("An account for that username already exists.");
        }

        User user = userRepository.save(User.builder()
                .username(authorizationRequest.getUsername())
                .password(passwordEncoder.encode(authorizationRequest.getPassword()))
                .build());

        resourceService.createUserRootFolder(user.getId());

        return UserDTO.from(user);
    }

    public UserDTO signIn(AuthorizationRequest authorizationRequest,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        User user = userRepository.findByUsername(authorizationRequest.getUsername());

        if (!passwordEncoder.matches(authorizationRequest.getPassword(), user.getPassword())) {
            throw new ValidationErrorException("Password doesn't match.");
        }

        storeAuthenticationToSession(authorizationRequest, request, response);

        return UserDTO.from(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public void storeAuthenticationToSession(AuthorizationRequest authorizationRequest,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authorizationRequest.getUsername(), authorizationRequest.getPassword())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
