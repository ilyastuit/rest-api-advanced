package com.epam.esm.service.tag.validation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagValidationErrorsSerializerTest {

    private static Writer jsonWriter = new StringWriter();
    private static JsonGenerator jsonGenerator;
    private static SerializerProvider serializerProvider;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();
    }

    @BeforeEach
    public void init() throws IOException {
        jsonGenerator.close();
        jsonWriter.close();
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
    }

    @Test
    void testValidatePojo() throws IOException {
        String message = "message";
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> errors = new HashMap<>();

        TagValidationErrors httpError = new TagValidationErrors(httpStatus, message, errors);
        new TagValidationErrorsSerializer().serialize(httpError, jsonGenerator, serializerProvider);

        jsonGenerator.flush();
        assertEquals(jsonWriter.toString(), "{\"status\":200,\"errorMessage\":\"message\",\"errors\":[{}]}");
    }

    @Test
    void testFieldError() throws IOException {
        String message = "Validation failed.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        errors.put("name", "name is required.");

        TagValidationErrors httpError = new TagValidationErrors(httpStatus, message, errors);
        new TagValidationErrorsSerializer().serialize(httpError, jsonGenerator, serializerProvider);

        jsonGenerator.flush();
        assertEquals(jsonWriter.toString(), "{\"status\":400,\"errorMessage\":\"Validation failed.\",\"errors\":[{\"name\":\"name is required.\"}]}");
    }

}
