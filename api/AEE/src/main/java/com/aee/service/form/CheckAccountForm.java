package com.aee.service.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckAccountForm {
    @NotEmpty(message = "firebaseToken can not be empty")
    @ApiModelProperty(name = "firebaseToken", required = true)
    private String firebaseToken;
    @NotEmpty(message = "firebaseUserId can not be empty")
    @ApiModelProperty(name = "firebaseUserId", required = true)
    private String firebaseUserId;
}
