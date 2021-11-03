package com.cos.jwt.controller;

import com.cos.jwt.model.User;
import com.cos.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody User user){

        return new ResponseEntity<>(authService.회원가입(user), HttpStatus.CREATED);
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refresh(HttpServletResponse response){
//
//    }

}
