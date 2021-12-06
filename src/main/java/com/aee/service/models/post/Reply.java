package com.aee.service.models.post;

import com.aee.service.models.BaseModel;
import com.aee.service.models.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Reply extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

    @Column(columnDefinition="TEXT")
    private String content;

}
