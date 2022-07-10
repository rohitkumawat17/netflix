package com.netflix.accessor.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class OtpDTO {
    private String otpId;
    private String userId;
    private String otp;
    private OtpState state;
    private Date createdAt;
    private OtpSentTo sentTo;
}
