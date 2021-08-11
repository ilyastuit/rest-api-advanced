package com.epam.esm.service.error;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Objects;

@JsonSerialize(using = HttpErrorSerializer.class)
public class HttpErrorImpl implements HttpError {

    private final HttpStatus status;
    private final String message;
    private final ErrorCode code;

    public HttpErrorImpl(String message, HttpStatus status, ErrorCode code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    @Override
    public String getCode() {
        return getStatus().value() + code.getCode();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, String> getErrors() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpErrorImpl httpError = (HttpErrorImpl) o;
        return status == httpError.status && message.equals(httpError.message) && code == httpError.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, code);
    }
}
