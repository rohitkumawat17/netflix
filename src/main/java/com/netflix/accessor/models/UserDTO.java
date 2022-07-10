package com.netflix.accessor.models;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phoneNo;
    private UserState userState;
    private UserRole role;
    private EmailVerificationStatus emailVerificationStatus;
    private PhoneVerificationStatus phoneVerificationStatus;
}
