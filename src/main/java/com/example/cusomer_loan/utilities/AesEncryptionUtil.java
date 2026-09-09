package com.example.cusomer_loan.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class AesEncryptionUtil {

    private final TextEncryptor encryptor;

    public AesEncryptionUtil(
            @Value("${app.encryption.secret}") String secret,
            @Value("${app.encryption.salt}") String salt) {
        this.encryptor = Encryptors.delux(secret, salt); // AES-GCM, authenticated
    }

    public String encrypt(String plainText) {
        return encryptor.encrypt(plainText);
    }

    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}