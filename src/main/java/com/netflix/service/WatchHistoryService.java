package com.netflix.service;

import com.netflix.accessor.VideoAccessor;
import com.netflix.accessor.WatchHistoryAccessor;
import com.netflix.accessor.models.UserDTO;
import com.netflix.accessor.models.UserRole;
import com.netflix.accessor.models.VideoDTO;
import com.netflix.accessor.models.WatchHistoryDTO;
import com.netflix.exception.InvalidVideoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.netflix.validator.Validator;

import java.nio.file.WatchService;

@Component
public class WatchHistoryService {

    @Autowired
    private Validator validator;

    @Autowired
    private WatchHistoryAccessor watchHistoryAccessor;

    @Autowired
    private VideoAccessor videoAccessor;

    public void updateWatchHistory(final String profileId, final String videoId, final int watchedLength) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        validator.validateProfile(profileId, userDTO.getUserId());
        validator.validateVideoId(videoId);
        VideoDTO videoDTO = videoAccessor.getVideoByVideoId(videoId);
        if(watchedLength < 1 || watchedLength > videoDTO.getTotalLength()) {
            throw new InvalidVideoException("Watched length of " + watchedLength + "is invalid!");
        }
        WatchHistoryDTO watchHistoryDTO = watchHistoryAccessor.getWatchHistory(profileId, videoId);
        if(watchHistoryDTO == null) {
            watchHistoryAccessor.insertWatchHistory(profileId, videoId, 0.0, watchedLength);
        } else {
            watchHistoryAccessor.updateWatchedLength(profileId, videoId, watchedLength);
        }
    }

    public int getWatchHistory(final String profileId, final String videoId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        validator.validateProfile(profileId, userDTO.getUserId());
        validator.validateVideoId(videoId);

        WatchHistoryDTO watchHistoryDTO = watchHistoryAccessor.getWatchHistory(profileId, videoId);
        if(watchHistoryDTO != null) {
            return watchHistoryDTO.getWatchLength();
        } else {
            return 0;
        }
    }

}
