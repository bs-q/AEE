package com.aee.service.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Api
public class CreateAccountForm {

    @NotEmpty(message = "password cant not be null")
    @ApiModelProperty(name = "password", required = true)
    private String password;
    @NotEmpty(message = "firebaseToken can not be empty")
    @ApiModelProperty(name = "firebaseToken", required = true)
    private String firebaseToken;
    @NotEmpty(message = "firebaseUserId can not be empty")
    @ApiModelProperty(name = "firebaseUserId", required = true)
    private String firebaseUserId;
}