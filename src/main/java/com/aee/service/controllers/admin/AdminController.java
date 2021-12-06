package com.aee.service.controllers.admin;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/a")
public class AdminController {
    @GetMapping("/content")
    public String getUser() {
        return "admin";
    }
}
