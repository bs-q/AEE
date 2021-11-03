package com.aee.service.models.university;

import com.aee.service.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class University extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    // khu vực, miền bắc, nam, ...
    private String zone;

    //Chuyên ngành đào tạo
    @OneToMany
    private List<Field> fields;


    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String website;

    @NotBlank
    // Mô tả về trường
    private String description;

    @NotBlank
    @OneToOne
    // kí hiệu của trường, ví dụ trường đại học sư phạm kĩ thuật -> UTE
    private Tag tag;

    // field này tạm thời để trống
    private Double rating;

}
