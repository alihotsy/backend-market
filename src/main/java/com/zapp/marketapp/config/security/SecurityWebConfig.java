package com.zapp.marketapp.config.security;

import com.zapp.marketapp.exceptions.handler.AccessDeniedHandlerImp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true,jsr250Enabled = true)
public class SecurityWebConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final AccessDeniedHandlerImp accessDeniedHandlerImp;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception, AccessDeniedException {
       return http.cors()
                .and()
                .csrf()
                .disable()
                .anonymous()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/categories", "/products", "/categories/id/{categoryId}", "/products/id/{productId}","/search/categories","/search/products","/uploads/categories/**","/uploads/products/**","/uploads/users/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST,"/auth/login", "/users/register","/auth/login/google")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/search/users")
                .hasAuthority("ADMIN_ROLE")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerImp)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();



    }

}
