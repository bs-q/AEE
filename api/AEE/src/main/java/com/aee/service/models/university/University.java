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
    private String description;

    @NotBlank
    @OneToOne
    private Tag tag;

    private Double rating;



}
