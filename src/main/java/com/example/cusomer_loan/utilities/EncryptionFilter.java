package com.example.cusomer_loan.utilities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class EncryptionFilter extends OncePerRequestFilter {

    private final AesEncryptionUtil encryptionUtil;
    private final ObjectMapper objectMapper;

    public EncryptionFilter(AesEncryptionUtil encryptionUtil, ObjectMapper objectMapper) {
        this.encryptionUtil = encryptionUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/test/"); // skip encryption for test endpoints
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // Decrypt incoming request body
        String decryptedRequestBody = decryptIncomingBody(request);
        HttpServletRequest decryptedRequestWrapper = wrapWithDecryptedBody(request, decryptedRequestBody);

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // Pass the DECRYPTED request wrapper down the chain, not the raw one
        chain.doFilter(decryptedRequestWrapper, wrappedResponse);
        // ☝️ EXECUTION PAUSES HERE, jumps into controller,
        //    and only returns to the NEXT line once controller is 100% done

        // Encrypt response body after controller logic runs
        byte[] responseBody = wrappedResponse.getContentAsByteArray();
        String plainJson = new String(responseBody, StandardCharsets.UTF_8);
        String encrypted = encryptionUtil.encrypt(plainJson);

        String finalOutput = "{\"data\":\"" + encrypted + "\"}";
        byte[] finalBytes = finalOutput.getBytes(StandardCharsets.UTF_8);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(finalBytes.length);
        response.getOutputStream().write(finalBytes);
        response.getOutputStream().flush();
    }

    private String decryptIncomingBody(HttpServletRequest request) throws IOException {
        String rawBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        if (rawBody.isBlank()) {
            return rawBody;
        }

        JsonNode node = objectMapper.readTree(rawBody);
        String encryptedData = node.get("data").asText();

        String decrypted = encryptionUtil.decrypt(encryptedData);
        System.out.println("DECRYPTED BODY: [" + decrypted + "]"); // <-- add brackets to spot leading/trailing whitespace
        return decrypted;
    }

    private HttpServletRequest wrapWithDecryptedBody(HttpServletRequest originalRequest, String decryptedBody) {
        byte[] bodyBytes = decryptedBody.getBytes(StandardCharsets.UTF_8);

        return new HttpServletRequestWrapper(originalRequest) {
            @Override
            public ServletInputStream getInputStream() {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyBytes);
                return new ServletInputStream() {
                    @Override
                    public int read() {
                        return byteArrayInputStream.read();
                    }

                    @Override
                    public boolean isFinished() {
                        return byteArrayInputStream.available() == 0;
                    }

                    @Override
                    public boolean isReady() {
                        return true;
                    }

                    @Override
                    public void setReadListener(ReadListener readListener) {
                        // not needed for this use case
                    }
                };
            }
            @Override
            public BufferedReader getReader() {
                return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
            }
            @Override
            public int getContentLength() {
                return bodyBytes.length;
            }
            @Override
            public long getContentLengthLong() {
                return bodyBytes.length;
            }
        };
    }
}