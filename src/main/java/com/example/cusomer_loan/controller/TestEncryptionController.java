package com.example.cusomer_loan.controller;

import com.example.cusomer_loan.utilities.AesEncryptionUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEncryptionController {

    private final AesEncryptionUtil encryptionUtil;

    public TestEncryptionController(AesEncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    @PostMapping("/test/encrypt")
    public String encrypt(@RequestBody String plainJson) {
        return encryptionUtil.encrypt(plainJson);
    }

    @PostMapping("/test/decrypt")
    public String decrypt(@RequestBody String encryptedText) {
        System.out.println("Encrypted text is: "+encryptedText);
        String decrypt = encryptionUtil.decrypt(encryptedText);
        System.out.println("Decripted text is: "+decrypt);
        return decrypt;
    }
}