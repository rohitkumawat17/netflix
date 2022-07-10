package com.netflix.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetWatchHistoryInput {
    private String profileId;
}
