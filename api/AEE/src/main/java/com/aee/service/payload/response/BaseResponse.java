package com.aee.service.payload.response;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private Boolean result = true;
    private String code = null;
    private T data = null;
    private String message = null;
}
