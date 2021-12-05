package com.aee.service.payload.response;

import lombok.Data;

@Data
public class FieldResponse {

    private Long id;
    // hệ đào tạo
    private String type;

    // giá tiền/chỉ
    private Double price;

    // điểm đầu vào
    private Double score;

    private Integer year;

    private String description;

    // tên ngành học, không được viết tắt
    private String name;

    // tag: ngành công nghệ thông tin, tag = IT
    private String tag;

    // số tín chỉ
    private Integer credits;

    private String universityName;

    private String universityImg;
}
