package com.aee.service.payload.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String fullName;
    private String avatarPath;
}
