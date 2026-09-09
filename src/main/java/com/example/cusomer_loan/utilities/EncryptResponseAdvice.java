package com.example.cusomer_loan.utilities;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    private final AesEncryptionUtil encryptionUtil;
    private final ObjectMapper objectMapper;

    public EncryptResponseAdvice(AesEncryptionUtil encryptionUtil, ObjectMapper objectMapper) {
        this.encryptionUtil = encryptionUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(Encrypted.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        try {
            String plainJson = objectMapper.writeValueAsString(body);
            String encrypted = encryptionUtil.encrypt(plainJson);

            EncryptedPayload envelope = new EncryptedPayload();
            envelope.setData(encrypted);
            return envelope; // client receives { "data": "..." }
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt response", e);
        }
    }
}