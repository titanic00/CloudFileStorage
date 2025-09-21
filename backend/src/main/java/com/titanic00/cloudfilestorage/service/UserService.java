package com.titanic00.cloudfilestorage.service;

import com.titanic00.cloudfilestorage.dto.UserDTO;
import com.titanic00.cloudfilestorage.entity.User;
import com.titanic00.cloudfilestorage.exception.AlreadyExists;
import com.titanic00.cloudfilestorage.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new AlreadyExists("User already exists.");
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        userRepository.save(newUser);

        return UserDTO.from(newUser);
    }

    public UserDTO signIn(User user) {
        return UserDTO.from(user);
    }

    public boolean userExists(User user) {
        return userRepository.findByUsername(user.getUsername()) != null;
    }
}
