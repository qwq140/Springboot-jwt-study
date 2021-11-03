package com.cos.jwt.service;

import com.cos.jwt.model.RoleType;
import com.cos.jwt.model.User;
import com.cos.jwt.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User 회원가입(User user){
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        Set<RoleType> roles = new HashSet<>();
        roles.add(RoleType.USER);
        user.setRoles(roles);
        return userRepository.save(user);
    }

}
