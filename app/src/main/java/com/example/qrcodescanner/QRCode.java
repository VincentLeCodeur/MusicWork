package com.example.qrcodescanner;

public class QRCode {
    private String code = "";

    public QRCode(String content) {
        this.code = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
