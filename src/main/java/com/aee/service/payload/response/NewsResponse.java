package com.aee.service.payload.response;

import lombok.Data;

@Data
public class NewsResponse {
    private Long id;

    private Integer type;

    private String titles;

    private String content;

    private String url;

    private String tag;

    private String thumbnail;
}
