package com.netflix.controller.model;

import com.netflix.accessor.models.PreferredAudience;
import com.netflix.accessor.models.ShowGenre;
import com.netflix.accessor.models.ShowType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShowOutput {
    private String showId;
    private String name;
    private ShowType typeOfShow;
    private ShowGenre genre;
    private PreferredAudience audience;
    private double rating;
    private int length;
    private String thumbnailLink;
}
