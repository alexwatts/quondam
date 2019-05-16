package com.ajw.bond.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
