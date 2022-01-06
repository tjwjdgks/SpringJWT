package com.study.springjwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springjwt.domain.User;
import com.study.springjwt.repository.UserRepository;
import com.study.springjwt.util.UserCostumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final UserCostumConverter userCostumConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/api/test")
    public String test(){
        return "success";
    }

    @PostMapping("/join")
    public String createUser(@RequestBody Map<String, Object> map) throws JsonProcessingException {
        User user  = userCostumConverter.mapToUser(map);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        save.getRoles().forEach(i->System.out.println(i.getType()));
        return "username "+save.getUsername() + "password " + save.getPassword();
    }
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
