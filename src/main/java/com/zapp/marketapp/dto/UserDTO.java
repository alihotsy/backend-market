package com.zapp.marketapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zapp.marketapp.utils.Bill;
import com.zapp.marketapp.utils.Roles;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class UserDTO implements UserDetails {


    private Integer userId;

    @NotBlank(message = "El campo nombre no debe ser vacío")
    private String name;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "Campo email no debe ser vacío")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, message = "El password debe contener 6 caracteres o más")
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.CLIENT_ROLE;

    //private List<ProductDTO> products_created = new ArrayList<>();

    @Embedded
    private Bill bill = new Bill();

    //private List<CategoryDTO> categories_created = new ArrayList<>();

    private String image;

    /*@NotBlank(message = "El campo address no debe ser vacío")
    private String address;*/

    @NotNull(message = "El campo google debe ser un valor booleano")
    private Boolean google = false;

    @NotNull(message = "El campo state debe ser un valor booleano")
    private Boolean state = true;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getUsername() {
        return email;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }
}
