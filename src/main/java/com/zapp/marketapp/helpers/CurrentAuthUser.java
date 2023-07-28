package com.zapp.marketapp.helpers;

import com.zapp.marketapp.dto.UserDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentAuthUser {

    public UserDTO getUserAuthenticated()  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Alguien anónimo intenta acceder a la aplicación!");
        }
        return (UserDTO) auth.getPrincipal();
    }

}
