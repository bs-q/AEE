package com.aee.service.models.audit;

import com.aee.service.security.services.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        try {
            UserDetailsImpl authentication = (UserDetailsImpl)securityContext.getAuthentication();
            String currentUser = null;
            if(authentication!=null){
                currentUser =  authentication.getUsername();
            }
            return Optional.of(currentUser!=null?currentUser:"admin");
        } catch (ClassCastException e){
            return Optional.of("reg");
        }

    }
}
