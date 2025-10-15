package com.titanic00.cloudfilestorage.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

    public UserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDetails) authentication.getPrincipal();
    }
}
