package com.aee.service.mapper;

import com.aee.service.models.news.News;
import com.aee.service.models.post.Post;
import com.aee.service.models.university.Field;
import com.aee.service.payload.response.FieldResponse;
import com.aee.service.payload.response.NewsResponse;
import com.aee.service.payload.response.PostResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NewsMapper {

    @Mapping(source = "tag.name",target = "tag")
    @Mapping(source = "thumbnail",target = "thumbnail")
    NewsResponse fromNewsToNewsResponse(News news);

    @IterableMapping(elementTargetType = NewsResponse.class)
    List<NewsResponse> fromNewsListToNewsResponseList(List<News> content);
}
