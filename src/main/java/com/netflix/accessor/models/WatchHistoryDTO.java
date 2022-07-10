package com.netflix.accessor.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class WatchHistoryDTO {
    private String profileId;
    private String videoId;
    private double rating;
    private int watchLength;
    private Date lastWatchedAt;
    private Date firstWatchedAt;
}
