package com.aee.service.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordRequest {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
}
