package com.aee.service.controllers.base;

import com.aee.service.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class BaseController {
    UserDetailsImpl userDetails;
    protected UserDetailsImpl getUserDetails(){
        if (userDetails == null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication!=null){
                userDetails = ((UserDetailsImpl) authentication.getPrincipal());
            }
        }
        return userDetails;
    }
}
