package com.example.cusomer_loan.utilities;

public class EncryptedPayload {
    private String data; // base64/hex encrypted JSON string

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}