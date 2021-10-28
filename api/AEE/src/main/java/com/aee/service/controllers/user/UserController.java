package com.aee.service.controllers.user;

import com.aee.service.payload.request.LoginRequest;
import com.aee.service.payload.response.BaseResponse;
import com.aee.service.payload.response.LoginResponse;
import com.aee.service.payload.response.ProfileResponse;
import com.aee.service.security.services.UserDetailsImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/u")
public class UserController {
    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<ProfileResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse<ProfileResponse> baseResponse = new BaseResponse<>();
        UserDetailsImpl userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            userDetails = ((UserDetailsImpl) authentication.getDetails());
            ProfileResponse response = new ProfileResponse();
            response.setFullName(userDetails.getUsername());
            response.setAvatarPath(userDetails.getAvatarPath());
            baseResponse.setData(response);
            baseResponse.setMessage("Profile success");
            baseResponse.setResult(true);
            return baseResponse;
        }
        return baseResponse;
    }




}
