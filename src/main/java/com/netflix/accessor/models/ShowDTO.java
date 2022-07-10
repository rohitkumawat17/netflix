package com.netflix.accessor.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class ShowDTO {
    private String showId;
    private String name;
    private ShowType typeOfShow;
    private ShowGenre genre;
    private PreferredAudience audience;
    private double rating;
    private int length;
    private String thumbnailPath;
}
