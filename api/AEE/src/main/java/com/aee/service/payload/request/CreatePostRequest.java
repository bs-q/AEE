package com.aee.service.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreatePostRequest{
    @NotNull
    private String title;

    @NotEmpty
    private String content;

}
