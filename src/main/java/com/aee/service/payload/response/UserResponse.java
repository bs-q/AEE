package com.aee.service.payload.response;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String email;
    private String avatarPath;
}
