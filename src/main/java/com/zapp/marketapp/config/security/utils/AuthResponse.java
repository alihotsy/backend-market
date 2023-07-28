package com.zapp.marketapp.config.security.utils;

import com.zapp.marketapp.dto.UserDTO;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse {

    private UserDTO user;
    private String token;

}
