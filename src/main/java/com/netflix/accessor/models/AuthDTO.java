package com.netflix.accessor.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class AuthDTO {
    private String authId;
    private String token;
    private String userId;
}
