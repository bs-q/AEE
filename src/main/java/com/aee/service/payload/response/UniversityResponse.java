package com.aee.service.payload.response;

import com.aee.service.models.university.Field;
import com.aee.service.models.university.Tag;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UniversityResponse {
    private Long id;

    private String name;

    private String address;

    private String zone;

    private String email;

    private String phoneNumber;


    private String website;


    private String description;


    private String image;

    private String tag;

}
