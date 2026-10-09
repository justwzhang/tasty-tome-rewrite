package com.justwzhang.tastytome.components.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justwzhang.tastytome.components.user.model.User;
import com.justwzhang.tastytome.components.user.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/test")
    public String test(){
        return "Hello World user";
    }
    @GetMapping("/me")
    public User me(@AuthenticationPrincipal Jwt jwt) {
        return userService.getOrCreateCurrentUser(jwt);
    }
}
