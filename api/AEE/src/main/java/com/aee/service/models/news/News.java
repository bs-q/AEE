package com.aee.service.models.news;

import com.aee.service.models.BaseModel;
import com.aee.service.models.university.Tag;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class News extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type of new
     * 1: news from other page
     * 0: news from admin page
     * */
    @NotBlank
    private Integer type;

    /**
     * Content of news
     * the content of new will null if news type equals to 1
     * the content will be in html string format
     * */
    private String content;


    /**
     * Url of news from other pages
     * */
    @Column(columnDefinition="TEXT")
    private String url;

    @Column(columnDefinition="TEXT")
    private String titles;

    @Column(columnDefinition="TEXT")
    private String thumbnail;

    /**
     * Tag of news
     * for searching purpose, tag must be match with university tag
     * */
    @NotBlank
    @ManyToOne
    private Tag tag;


}
