package com.aee.service.mapper;

import com.aee.service.form.CreateAccountForm;
import com.aee.service.models.User;
import com.aee.service.models.post.Reply;
import com.aee.service.payload.response.ReplyResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChatMapper {
    @Mapping(source = "content", target = "content")
    @Mapping(source = "user.email", target = "user")
    @Mapping(source = "createdDate",target = "createdDate")
    @BeanMapping(ignoreByDefault = true)
    ReplyResponse fromReplyToReplyResponse(Reply reply);

    @IterableMapping(elementTargetType = ReplyResponse.class)
    List<ReplyResponse> fromReplyListToReplyResponseList(List<Reply> content);
}
