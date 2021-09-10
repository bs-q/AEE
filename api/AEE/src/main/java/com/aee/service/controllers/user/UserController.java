package com.aee.service.controllers.user;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/u")
public class UserController {
    @GetMapping(value = "/content", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUser() {
        return "user";
    }
}
