package com.srinadh.medsync.controller;

import com.srinadh.medsync.dto.LoginRequest;
import com.srinadh.medsync.dto.LoginResponse;
import com.srinadh.medsync.dto.RegisterRequest;
import com.srinadh.medsync.entity.User;
import com.srinadh.medsync.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

//    @PostMapping("/login")
//    public Map<String,String> login(
//            @RequestBody LoginRequest request){
//
//        String token = authService.login(request);
//
//        return Map.of(
//                "token",
//                token
//        );
//
//    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}