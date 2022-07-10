package com.netflix.mapper;

import com.netflix.accessor.S3Accessor;
import com.netflix.accessor.models.ShowDTO;
import com.netflix.controller.model.ShowOutput;
import org.springframework.beans.factory.annotation.Autowired;

public class ShowMapper {

    @Autowired
    private S3Accessor s3Accessor;

    public ShowOutput mapShowDtoToOutput(final ShowDTO input) {
        ShowOutput output = ShowOutput.builder()
                .showId(input.getShowId())
                .name(input.getName())
                .typeOfShow(input.getTypeOfShow())
                .genre(input.getGenre())
                .audience(input.getAudience())
                .rating(input.getRating())
                .length(input.getLength())
                .thumbnailLink(s3Accessor.getPreSignedUrl(input.getThumbnailPath(), 5*60))
                .build();
        return output;

    }
}
