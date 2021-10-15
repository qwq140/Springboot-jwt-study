package com.cos.jwt.controller;

import com.cos.jwt.controller.dto.CMRespDto;
import com.cos.jwt.model.User;
import com.cos.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public CMRespDto<?> join(@RequestBody User user){
        return new CMRespDto<>(1,"회원가입 성공",userService.회원가입(user));
    }

}
