package com.cos.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping ("token")
    public String token(){
        return "<h1>token</h1>";
    }

    // user, manager, admin 권한만 접근가능
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    // manager, admin 권한만 접근가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    // admin 권한만 접근가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
