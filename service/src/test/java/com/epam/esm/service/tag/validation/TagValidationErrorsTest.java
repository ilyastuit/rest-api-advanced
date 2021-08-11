package com.epam.esm.service.tag.validation;

import com.epam.esm.service.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagValidationErrorsTest {

    @Test
    void testAssertionEmptyMap() {
        String message = "message";
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> errors = new HashMap<>();

        TagValidationErrors httpError = new TagValidationErrors(httpStatus, message, errors);

        assertTrue(httpError.getErrors().isEmpty());
        assertEquals(httpError.getMessage(), message);
        assertEquals(httpError.getStatus(), httpStatus);
        assertEquals(httpError.getCode(), httpStatus.value() + ErrorCode.TAG.getCode());
    }

    @Test
    void testAssertionWithMap() {
        String message = "message";
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "error");

        TagValidationErrors httpError = new TagValidationErrors(httpStatus, message, errors);

        assertFalse(httpError.getErrors().isEmpty());
        assertEquals(httpError.getErrors().get("error"), errors.get("error"));
        assertEquals(httpError.getMessage(), message);
        assertEquals(httpError.getStatus(), httpStatus);
        assertEquals(httpError.getCode(), httpStatus.value() + ErrorCode.TAG.getCode());
    }

}
