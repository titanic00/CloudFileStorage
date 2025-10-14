package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.dto.UserDTO;
import com.titanic00.cloudfilestorage.dto.request.AuthorizationRequest;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.exception.AlreadyExistsException;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDTO signUp(AuthorizationRequest authorizationRequest) {
        if (usernameExists(authorizationRequest.getUsername())) {
            throw new AlreadyExistsException("An account for that username already exists.");
        }

        User user = userRepository.save(User.builder()
                .username(authorizationRequest.getUsername())
                .password(passwordEncoder.encode(authorizationRequest.getPassword()))
                .build());

        return UserDTO.from(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
