package com.titanic00.cloudfilestorage.dto;

import com.titanic00.cloudfilestorage.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;

    public static UserDTO from(User user) {
        return builder()
                .username(user.getUsername())
                .build();
    }
}
