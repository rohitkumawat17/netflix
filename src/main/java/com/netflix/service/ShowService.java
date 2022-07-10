package com.netflix.service;

import com.netflix.accessor.ShowAccessor;
import com.netflix.accessor.models.ShowDTO;
import com.netflix.controller.model.ShowOutput;
import com.netflix.mapper.ShowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShowService {

    @Autowired
    private ShowAccessor showAccessor;

    @Autowired
    private ShowMapper showMapper;

    public List<ShowOutput> getShowsByName(String showName) {
        List<ShowDTO> showDTOList = showAccessor.getShowsByName(showName);
        List<ShowOutput> showOutput = new ArrayList<>();

        for(ShowDTO showDTO: showDTOList) {
            showOutput.add(showMapper.mapShowDtoToOutput(showDTO));
        }

        return showOutput;
    }
}
