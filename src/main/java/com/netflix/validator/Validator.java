package com.netflix.validator;

import com.netflix.accessor.ProfileAccessor;
import com.netflix.accessor.VideoAccessor;
import com.netflix.accessor.models.ProfileDTO;
import com.netflix.accessor.models.VideoDTO;
import com.netflix.exception.InvalidProfileException;
import com.netflix.exception.InvalidVideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    @Autowired
    ProfileAccessor profileAccessor;

    @Autowired
    VideoAccessor videoAccessor;

    public void validateProfile(final String profileId, final String userId) {
        ProfileDTO profileDTO = profileAccessor.getProfileByProfileId(profileId);
        if(profileDTO == null || !profileDTO.getUserId().equals(userId)) {
            throw new InvalidProfileException("Profile " + profileId + " is invalid or doesn't exist!");
        }
    }

    public void validateVideoId(final String videoId) {
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if(videoId == null) {
            throw new InvalidVideoException("Video with videoId " + videoId + " does not exist!");
        }
    }

}
