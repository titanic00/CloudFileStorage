package com.titanic00.cloudfilestorage.dto;

import com.titanic00.cloudfilestorage.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String username;

    public static UserDTO from(User user) {
        return UserDTO.builder().username(user.getUsername()).build();
    }
}
