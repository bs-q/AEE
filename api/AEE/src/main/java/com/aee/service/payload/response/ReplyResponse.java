package com.aee.service.payload.response;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyResponse {
    private String content;
    private String user;
    private Date createdDate;
}
