package com.example.cusomer_loan.controller;

import com.example.cusomer_loan.dtos.LoginRequest;
import com.example.cusomer_loan.dtos.LoginResponse;
import com.example.cusomer_loan.utilities.Encrypted;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    @Encrypted // <-- triggers both decrypt-on-in, encrypt-on-out
    public LoginResponse login(@RequestBody LoginRequest request) {
        // request is already DECRYPTED here — write normal business logic
        System.out.println("Username: " + request.getUsername());

        LoginResponse response = new LoginResponse();
        response.setToken("jwt-token-here");
        return response; // gets AUTO-ENCRYPTED before leaving
    }
}