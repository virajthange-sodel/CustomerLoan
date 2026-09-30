package com.example.cusomer_loan.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class AesEncryptionUtil {

    private final BytesEncryptor encryptor;

    public AesEncryptionUtil(
            @Value("${app.encryption.secret}") String secret,
            @Value("${app.encryption.salt}") String salt) {
//        this.encryptor = Encryptors.delux(secret, salt); // AES-GCM, authenticated
        this.encryptor = AesGcmBytesEncryptor.withPassword(secret, salt).build();
    }

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            log.info("Plaintext is empty");
            return plainText;
        }
        byte[] encryptedBytes = encryptor.encrypt(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
//        return encryptor.encrypt(plainText);
    }

//    Comments
    public String decrypt(String encryptedBase64) {
        if (encryptedBase64 == null || encryptedBase64.isBlank()) {
            throw new IllegalArgumentException("Encrypted payload cannot be empty");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedBase64);
        byte[] decryptedBytes = encryptor.decrypt(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
//        return encryptor.decrypt(encryptedText);
    }
}