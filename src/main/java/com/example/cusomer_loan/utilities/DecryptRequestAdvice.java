package com.example.cusomer_loan.utilities;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class DecryptRequestAdvice implements RequestBodyAdvice {

    private final AesEncryptionUtil encryptionUtil;
    private final ObjectMapper objectMapper;

    public DecryptRequestAdvice(AesEncryptionUtil encryptionUtil, ObjectMapper objectMapper) {
        this.encryptionUtil = encryptionUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // Only apply to endpoints annotated with @Encrypted (see step below)
        return methodParameter.hasMethodAnnotation(Encrypted.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {

        // Read the encrypted envelope { "data": "..." }
        EncryptedPayload envelope = objectMapper.readValue(inputMessage.getBody(), EncryptedPayload.class);

        // Decrypt to get the real JSON
        String decryptedJson = encryptionUtil.decrypt(envelope.getData());

        // Return a new InputMessage with the decrypted JSON,
        // so Spring's normal Jackson conversion picks it up from here
        InputStream decryptedStream = new ByteArrayInputStream(decryptedJson.getBytes(StandardCharsets.UTF_8));

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() { return decryptedStream; }

            @Override
            public HttpHeaders getHeaders() { return inputMessage.getHeaders(); }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}