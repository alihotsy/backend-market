package com.zapp.marketapp.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.zapp.marketapp.config.security.JwtAuthService;
import com.zapp.marketapp.config.security.utils.AuthCredentials;
import com.zapp.marketapp.config.security.utils.AuthResponse;
import com.zapp.marketapp.config.security.utils.RenewToken;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.UserMapper;
import com.zapp.marketapp.entities.User;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.helpers.CurrentAuthUser;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.GoogleToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtAuthService jwtAuthService;
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;
    private final CurrentAuthUser currentAuthUser;
    private final JpaUser jpaUser;
    private final UserMapper userMapper;

    @Value("${client_id}")
    private String clientId;

    public AuthResponse login(AuthCredentials credentials) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());
        UserDTO userFound = (UserDTO) userDetails;

        if(!passwordEncoder.matches(credentials.getPassword(), userDetails.getPassword())) {
            throw new UsernameNotFoundException("Password inválido");
        }

        Integer userId = userFound.getUserId();

        return AuthResponse.builder()
                .user(userFound)
                .token(jwtAuthService.generateToken(userFound, Map.of("uid",userId)))
                .build();
    }

    public AuthResponse renewToken() {
        UserDTO user = currentAuthUser.getUserAuthenticated();

        return AuthResponse.builder()
                .user(user)
                .token(jwtAuthService.generateToken(user,Map.of("uid",user.getUserId())))
                .build();
    }

    public AuthResponse authByGoogle(GoogleToken token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(),new GsonFactory())
                .setAudience(List.of(clientId))
                .build();

        GoogleIdToken idToken =  verifier.verify(token.getToken());

        if(idToken == null) {
           throw new IllegalArgumentException("Token de Google inválido");
        }

        Payload payload = idToken.getPayload();

        jpaUser.findByEmailAndState(payload.getEmail(),false)
                        .ifPresent(userDeleted -> {
                            throw new EntityException(false,"Este usuario ha sido bloqueado","state",400,"User");
                        });

        jpaUser.findByEmailAndStateAndGoogle(payload.getEmail(),true,false)
                .ifPresent(user -> {
                    throw new EntityException(false,"Ya existe una cuenta con ese email","payload_email",400,"User");
                });

        UserDTO user = jpaUser.findByEmailAndStateAndGoogle(payload.getEmail(),true,true)
                .map(userMapper::toUserDTO)
                .orElseGet(() -> {
                    String name = (String) payload.get("name");
                    String image = (String) payload.get("picture");
                    User newUser = new User();
                    newUser.setEmail(payload.getEmail());
                    newUser.setName(name);
                    newUser.setImg(image);
                    newUser.setGoogle(true);
                    newUser.setPassword("<No-password>");
                    return userMapper.toUserDTO(jpaUser.save(newUser));
                });

        return AuthResponse.builder()
                .user(user)
                .token(jwtAuthService.generateToken(user,Map.of("uid",user.getUserId())))
                .build();
    }
}
