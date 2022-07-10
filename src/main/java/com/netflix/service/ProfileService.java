package com.netflix.service;

import com.netflix.accessor.ProfileAccessor;
import com.netflix.accessor.models.ProfileType;
import com.netflix.accessor.models.UserDTO;
import com.netflix.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.netflix.controller.model.ProfileTypeInput;

public class ProfileService {

    @Autowired
    ProfileAccessor profileAccessor;

    public void activateProfile(final String name, final ProfileTypeInput type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        if(name.length() < 5 || name.length() > 20) {
            throw new InvalidDataException("Name length should be b/w 5 and 20");
        }
        profileAccessor.addNewProfile(userDTO.getUserId(),name,ProfileType.valueOf(type.name()));
    }

    public void deactivateProfile(final String profileId) {
        profileAccessor.deleteProfile(profileId);
    }
}
