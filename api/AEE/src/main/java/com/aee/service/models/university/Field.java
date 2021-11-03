package com.aee.service.models.university;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
