package com.netflix.controller;

import com.netflix.exception.InvalidCredentialsException;
import com.netflix.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.controller.model.LoginInput;
import org.springframework.security.access.annotation.Secured;
import com.netflix.security.Roles;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    public ResponseEntity<String> login(@RequestBody LoginInput loginInput) {
        String email = loginInput.getEmail();
        String password = loginInput.getPassword();

        try {
            String token = authService.login(email, password);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (InvalidCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/logoutCurrentUser")
    @Secured({Roles.User,Roles.Customer})
    public void logout(@RequestHeader(name="Authorization",required = false) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ","");
        authService.logout(token);
    }

}
