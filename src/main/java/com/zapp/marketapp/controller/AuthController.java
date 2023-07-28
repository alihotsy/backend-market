package com.zapp.marketapp.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.zapp.marketapp.config.security.utils.AuthCredentials;
import com.zapp.marketapp.config.security.utils.AuthResponse;
import com.zapp.marketapp.config.security.utils.RenewToken;
import com.zapp.marketapp.service.AuthService;
import com.zapp.marketapp.utils.GoogleToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthCredentials credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }

    @PostMapping("/login/google")
    public ResponseEntity<Object> loginByGoogle(@RequestBody GoogleToken token) throws GeneralSecurityException, IOException {

        return ResponseEntity.ok(authService.authByGoogle(token));

    }

    @GetMapping("/renew-token")
    public ResponseEntity<AuthResponse> renewToken() {
        return ResponseEntity.ok(authService.renewToken());
    }


}
