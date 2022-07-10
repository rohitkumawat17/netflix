package com.netflix.controller;

import com.netflix.accessor.models.ShowDTO;
import com.netflix.controller.model.ShowOutput;
import com.netflix.exception.DependencyFailureException;
import com.netflix.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.security.Roles;

import java.util.List;

@RestController
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/show")
    @Secured({Roles.Customer})
    public ResponseEntity<List<ShowOutput>> getShowbyName(@RequestParam("showName") String showName) {
        try {
            List<ShowOutput> showList = showService.getShowsByName(showName);
            return ResponseEntity.status(HttpStatus.OK).body(showList);
        }
        catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
