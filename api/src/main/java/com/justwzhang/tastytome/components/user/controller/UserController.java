package com.justwzhang.tastytome.components.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("/test")
    public String test(){
        return "Hello World user";
    }
    @GetMapping("/me")
    public String me(@AuthenticationPrincipal Jwt jwt) {
        return "Hello " + jwt.getClaimAsString("preferred_username");
    }
}
