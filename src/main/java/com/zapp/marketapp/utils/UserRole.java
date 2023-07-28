package com.zapp.marketapp.utils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.EnumSet;


@Getter
public class UserRole {

    @NotNull(message = "Rol inválido. Solo se permite ADMIN_ROLE,CLIENT_ROLE,INVENTORY_ROLE")
    @Enumerated(EnumType.STRING)
    private Roles role;

    public void setRole(String role) {
        EnumSet<Roles> roles = EnumSet.allOf(Roles.class);
        role = String.valueOf(role);
        try{
            boolean isValid = roles.contains(Roles.valueOf(role));
            this.role = Roles.valueOf(role);
        }catch (IllegalArgumentException e){
            this.role = null;
        }


    }
}
