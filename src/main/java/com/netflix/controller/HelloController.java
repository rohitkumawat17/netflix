package com.netflix.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.security.Roles;
@RestController
public class HelloController {

    @GetMapping("/hello")
    //@Secured({Roles.Customer, Roles.Anonymous})
    public String sayHello() {
        return "Hello it's me!";
    }
}
