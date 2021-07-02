package com.epam.esm.service.error;

public enum ErrorCode {
    GIFT_CERTIFICATE("01"), TAG("02");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
