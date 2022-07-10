package com.netflix.controller;

import com.netflix.accessor.models.UserDTO;
import com.netflix.controller.model.CreateUserInput;
import com.netflix.controller.model.VerifyEmailInput;
import com.netflix.controller.model.VerifyPhoneNoInput;
import com.netflix.exception.InvalidDataException;
import com.netflix.exception.UserNotFoundException;
import com.netflix.security.Roles;
import com.netflix.security.SecurityConstants;
import com.netflix.service.AuthService;
import com.netflix.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@ControllerAdvice
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Secured({ Roles.Customer, Roles.User })
    @GetMapping("/passwordLink")
    public ResponseEntity<Boolean> sendResetPasswordLink(@RequestParam("email") String email) {
        try {
            System.out.println("Got a request for reset password link");
            userService.sendResetPasswordLink(email);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        catch(UserNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @Secured({ Roles.Customer, Roles.User })
    @PutMapping("/password")
    public boolean updatePassword(@RequestParam("userId") String userId, @RequestParam("password") String newPassword) {
        return userService.updatePassword(userId, newPassword);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO != null && userDTO.getPassword().equals(password)) {
            Date expirationDate = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
            String token = Jwts.builder()
                    .setSubject(email)
                    .setAudience(userDTO.getRole().toString())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY.getBytes())
                    .compact();
            if (authService.storeToken(userDTO.getUserId(), token)) {
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to store the token!");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
    }

    @PostMapping("/user")
    public ResponseEntity<String> addNewUser(@RequestBody CreateUserInput createUserInput) {
        String name = createUserInput.getName();
        String email = createUserInput.getEmail();
        String password = createUserInput.getPassword();
        String phoneNo = createUserInput.getPhoneNo();
        try {
            userService.addNewUser(email, name, password, phoneNo);
        } catch (InvalidDataException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO != null && userDTO.getPassword().equals(password)) {
            Date expirationDate = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
            String token = Jwts.builder()
                    .setSubject(email)
                    .setAudience(userDTO.getRole().toString())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY.getBytes())
                    .compact();
            if (authService.storeToken(userDTO.getUserId(), token)) {
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to store the token!");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
    }

    @PostMapping("/user/subscription")
    @Secured({Roles.User})
    public String activateSubscription() {
        userService.activateSubscription();
        return "Subscription Activated Successfully.";
    }

    @DeleteMapping("/user/subscription")
    @Secured({Roles.Customer})
    public String deleteSubscription() {
        userService.activateSubscription();
        return "Subscription Deleted Successfully.";
    }

    @PostMapping("/user/email")
    @Secured({Roles.User, Roles.Customer})
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailInput emailInput) {
        try {
            userService.verifyEmail(emailInput.getOtp());
            return ResponseEntity.status(HttpStatus.OK).body("Otp verified successfully!");
        } catch (InvalidDataException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/user/email")
    @Secured({Roles.User, Roles.Customer})
    public ResponseEntity<String> getEmailOtp() {
        try {
            userService.sendEmailOtp();
            return ResponseEntity.status(HttpStatus.OK).body("Otp sent successfully!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/user/phoneNo")
    @Secured({Roles.User, Roles.Customer})
    public ResponseEntity<String> verifyPhoneNo(@RequestBody VerifyPhoneNoInput phoneNoInput) {
        try {
            userService.verifyEmail(phoneNoInput.getOtp());
            return ResponseEntity.status(HttpStatus.OK).body("Otp verified successfully!");
        } catch (InvalidDataException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
