package com.aee.service.models.post;

import com.aee.service.models.BaseModel;
import com.aee.service.models.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Post extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User creator;

    @Column(columnDefinition="TEXT")
    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    private Integer vote;


}
