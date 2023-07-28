package com.zapp.marketapp.config.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapp.marketapp.helpers.Validators;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthService jwtAuthService;
    private final UserDetailsService userDetailsService;
    private final List<String> paths;
    private final Validators validators;

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String requestUrl = request.getServletPath();


        if(
             paths.stream().anyMatch(
                     path -> requestUrl.contains(path) && !requestUrl.contains("/update") //Acepta rutas públicas de la aplicación.
             )
        ) {
            filterChain.doFilter(request,response);
            return;
        }

        if(Objects.isNull(header) || header.trim().isEmpty()) {
            validators.jsonError("Se requiere un token",response);
            return;
        }


        try{
            jwtAuthService.verifyToken().verify(header);
        }catch (JWTVerificationException e) {
            validators.jsonError(e.getMessage(),response);
            return;
        }catch (SignatureException e) {
            validators.jsonError("La firma es inválida - SignatureException",response);
            return;
        }


        final UserDetails getUser;
        try{
            final String email = jwtAuthService.getEmail(header);
            getUser = userDetailsService.loadUserByUsername(email);

        }catch (UsernameNotFoundException e) {
            validators.jsonError(e.getMessage(),response);
            return;
        }


        if(!jwtAuthService.isTokenSubjectValid(getUser,header)) {
            validators.jsonError("El subject es inválido",response);
            return;
        }

        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("Usuario ya está autenticado");
            filterChain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                getUser,null, getUser.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);

    }
}
