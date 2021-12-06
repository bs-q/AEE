package com.aee.service.payload.request;

import lombok.Data;

@Data
public class ReplyRequest {
    private Long postId;
    private String content;
}
