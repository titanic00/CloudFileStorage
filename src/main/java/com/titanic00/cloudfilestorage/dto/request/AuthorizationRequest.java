package com.titanic00.cloudfilestorage.dto.request;

import lombok.Data;

@Data
public class AuthorizationRequest {
    private String username;
    private String password;
}
