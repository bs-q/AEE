package com.aee.service.payload.response;

import lombok.Data;

@Data
public class BasePagingResponse <T>{
    private T data;
    private Integer currentPage;
    private Integer totalItems;
    private Integer totalPages;
    private Boolean result = false;
}
