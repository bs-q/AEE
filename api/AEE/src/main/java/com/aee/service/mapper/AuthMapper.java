package com.aee.service.mapper;

import com.aee.service.form.CreateAccountForm;
import com.aee.service.models.User;
import com.aee.service.payload.response.LoginResponse;
import com.aee.service.security.services.UserDetailsImpl;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firebaseToken", target = "token")
    @Mapping(source = "firebaseUserId", target = "uid")
    @BeanMapping(ignoreByDefault = true)
    User fromCreateFormToAccount(CreateAccountForm createCustomerForm);

    @Mapping(source = "username", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    LoginResponse fromUserToLoginResponse(UserDetailsImpl user);
}
