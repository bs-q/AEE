package com.aee.service.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Api
public class CreateAccountForm {
    @NotEmpty(message = "firebaseToken can not be empty")
    @ApiModelProperty(name = "firebaseToken", required = true)
    private String firebaseToken;
    @NotEmpty(message = "firebaseUserId can not be empty")
    @ApiModelProperty(name = "firebaseUserId", required = true)
    private String firebaseUserId;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    @NotEmpty(message = "password cant not be null")
    @ApiModelProperty(name = "password", required = true)
    private String password;
}