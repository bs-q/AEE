package com.aee.service.mapper;

import com.aee.service.form.CreateAccountForm;
import com.aee.service.models.User;
import com.aee.service.payload.response.LoginResponse;
import com.aee.service.security.services.UserDetailsImpl;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-19T22:10:26+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public User fromCreateFormToAccount(CreateAccountForm createCustomerForm) {
        if ( createCustomerForm == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( createCustomerForm.getEmail() );
        user.setToken( createCustomerForm.getFirebaseToken() );
        user.setUid( createCustomerForm.getFirebaseUserId() );

        return user;
    }

    @Override
    public LoginResponse fromUserToLoginResponse(UserDetailsImpl user) {
        if ( user == null ) {
            return null;
        }

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setFullName( user.getUsername() );
        loginResponse.setAvatarPath( user.getAvatarPath() );

        return loginResponse;
    }
}
