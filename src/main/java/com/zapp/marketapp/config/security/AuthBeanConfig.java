package com.zapp.marketapp.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapp.marketapp.dto.mapper.UserMapper;
import com.zapp.marketapp.repository.JpaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class AuthBeanConfig  {

    private final JpaUser jpaUser;
    private final UserMapper userMapper;


    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build();
    }

    @Bean
    public UserDetailsService loadUserByUsername()  {
        return (username) -> jpaUser.findByEmailAndState(username,true).map(userMapper::toUserDTO)
                 .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ese email"));
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(loadUserByUsername());
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;
    }

    @Bean
    public List<String> paths() { //Rutas públicas que no necesitan token de autenticación
        return List.of(
                "/users/register",
                "/auth/login",
                "/categories",
                "/products",
                "/auth/login/google",
                "/search/categories",
                "/search/products",
                "/uploads/products",
                "/uploads/categories",
                "/uploads/users",
                "/id"
        );
    }



}
