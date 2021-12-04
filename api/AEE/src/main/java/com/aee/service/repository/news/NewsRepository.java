package com.aee.service.repository.news;

import com.aee.service.models.news.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News,Long> {

}