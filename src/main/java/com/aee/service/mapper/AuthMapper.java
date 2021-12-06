package com.aee.service.mapper;

import com.aee.service.form.CreateAccountForm;
import com.aee.service.models.User;
import com.aee.service.payload.response.LoginResponse;
import com.aee.service.payload.response.UserResponse;
import com.aee.service.security.services.UserDetailsImpl;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firebaseUserId", target = "uid")
    @BeanMapping(ignoreByDefault = true)
    User fromCreateFormToAccount(CreateAccountForm createCustomerForm);

    @Mapping(source = "username", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    LoginResponse fromUserToLoginResponse(UserDetailsImpl user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "email", target = "email")
    @BeanMapping(ignoreByDefault = true)
    User fromUserDetailToUser(UserDetailsImpl user);

    @Mapping(source = "username",target = "username")
    @Mapping(source = "avatarPath",target = "avatarPath")
    @Mapping(source = "email",target = "email")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromUserToUserResponse")
    UserResponse fromUserToUserResponse(User user);

    @Mapping(source = "username", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    LoginResponse fromUserToLoginResponse(User user);
}
