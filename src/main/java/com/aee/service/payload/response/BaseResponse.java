package com.aee.service.payload.response;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private Boolean result = false;
    private String code = null;
    private T data = null;
    private String message = null;
}
