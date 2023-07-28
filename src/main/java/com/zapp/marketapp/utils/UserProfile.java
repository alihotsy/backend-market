package com.zapp.marketapp.utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
    @NotBlank(message = "El campo nombre no debe ser vacío")
    private String name;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "Campo email no debe ser vacío")
    private String email;

    /*@NotBlank(message = "El campo address no debe ser vacío")
    private String address;*/
}
