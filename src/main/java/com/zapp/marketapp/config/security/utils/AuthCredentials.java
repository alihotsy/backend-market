package com.zapp.marketapp.config.security.utils;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthCredentials {

    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El password es obligatorio")
    private String password;
}
