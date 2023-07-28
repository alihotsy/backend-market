package com.zapp.marketapp.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class JwtAuthService {
    @Value("${key}")
    private String PRIVATE_KEY;

    private Claims getAllClaims(String token) { //Los claims heredan de Map<String,Object>, por lo tanto es un Map
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T getClaim(String token, Function<Claims,T> resolver) {
        return resolver.apply(getAllClaims(token));
    }

    public String generateToken(UserDetails userDetails, Map<String,Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .signWith(getSignInKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 43200000))
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String getEmail(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Integer getUserId(String token) {
        return getAllClaims(token).get("uid", Integer.class);
    }

    public JWTVerifier verifyToken() throws SignatureException {
        return JWT.require(Algorithm.HMAC384(Decoders.BASE64.decode(PRIVATE_KEY)))
                .build();
    }

    boolean isTokenExpired(String token) {
        return getClaim(token,Claims::getExpiration).before(new Date());
    }

    boolean isTokenSubjectValid(UserDetails userDetails, String token) {
        return (userDetails.getUsername().equals(getEmail(token)) && !isTokenExpired(token));
    }


    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(PRIVATE_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

}
