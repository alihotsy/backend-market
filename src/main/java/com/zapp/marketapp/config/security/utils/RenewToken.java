package com.zapp.marketapp.config.security.utils;

import com.zapp.marketapp.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenewToken {

    private UserDTO user;
    private String token;
}
