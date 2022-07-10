package com.netflix.service;

import com.netflix.accessor.AuthAccessor;
import com.netflix.accessor.UserAccessor;
import com.netflix.accessor.models.AuthDTO;
import com.netflix.accessor.models.UserDTO;
import com.netflix.exception.DependencyFailureException;
import com.netflix.exception.InvalidCredentialsException;
import com.netflix.exception.InvalidVideoException;
import com.netflix.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthService {

    @Autowired
    private UserAccessor userAccessor;

    @Autowired
    private AuthAccessor authAccessor;

    public String login(final String email, final String password) {
        UserDTO userDTO = userAccessor.getUserByEmail(email);
            if (userDTO != null && userDTO.getPassword().equals(password)) {
                String token = Jwts.builder()
                        .setSubject(email)
                        .setAudience(userDTO.getRole().name())
                        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY.getBytes())
                        .compact();
                authAccessor.storeToken(userDTO.getUserId(), token);
                return token;
            }
            throw new InvalidCredentialsException("Either the Email & password is incorrect!");
    }

    public AuthDTO findByToken(String token) {
        return authAccessor.findByToken(token);
    }

    public boolean storeToken(String userId, String token) {
        return authAccessor.storeToken(userId, token);
    }

    public void logout(final String token) {
        authAccessor.deleteAuthByToken(token);
    }
}