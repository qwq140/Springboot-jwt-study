package com.cos.jwt.service;

import com.cos.jwt.model.User;
import com.cos.jwt.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User 회원가입(User user){
        return userRepository.save(user);
    }

}
