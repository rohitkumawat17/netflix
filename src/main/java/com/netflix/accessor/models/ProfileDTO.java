package com.netflix.accessor.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ProfileDTO {
    private String profileId;
    private String  name;
    private ProfileType type;
    private Date createdAt;
    private String userId;
}
