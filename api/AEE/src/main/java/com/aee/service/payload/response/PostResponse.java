package com.aee.service.payload.response;

import com.aee.service.models.User;
import lombok.Data;

import java.util.Date;

@Data
public class PostResponse {
    private Date createdDate;
    private String title;
    private String content;
    private Long id;
    private UserResponse user;
}
