package com.epam.esm.service.exceptions;

import com.epam.esm.service.error.ErrorCode;
import com.epam.esm.service.error.HttpError;
import com.epam.esm.service.error.HttpErrorImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CustomResponseEntityExceptionHandlerTest {

    private final CustomResponseEntityExceptionHandler handler = new CustomResponseEntityExceptionHandler();

    @Test
    void successTagNotFoundHandler() {
        TagNotFoundException exception = new TagNotFoundException(1);
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.TAG);

        final ResponseEntity<HttpError> handled = handler.handleTagNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void failTagNotFoundHandlerWithDifferentStatus() {
        TagNotFoundException exception = new TagNotFoundException("name");
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.OK, ErrorCode.TAG);

        final ResponseEntity<HttpError> handled = handler.handleTagNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertFalse(httpError.equals(handled.getBody()));
        assertNotEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void successGiftCertificateNotFoundHandler() {
        GiftCertificateNotFoundException exception = new GiftCertificateNotFoundException(1);
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.GIFT_CERTIFICATE);

        final ResponseEntity<HttpError> handled = handler.handleGiftCertificateNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void failGiftCertificateNotFoundHandlerWithDifferentStatus() {
        GiftCertificateNotFoundException exception = new GiftCertificateNotFoundException(2);
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.OK, ErrorCode.GIFT_CERTIFICATE);

        final ResponseEntity<HttpError> handled = handler.handleGiftCertificateNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertFalse(httpError.equals(handled.getBody()));
        assertNotEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void successTagNameAlreadyExistExceptionHandler() {
        TagNameAlreadyExistException exception = new TagNameAlreadyExistException("name");
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.CONFLICT, ErrorCode.TAG);

        final ResponseEntity<HttpError> handled = handler.handleTagNameAlreadyExistException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void failTagNameAlreadyExistExceptionHandlerWithDifferentStatus() {
        TagNameAlreadyExistException exception = new TagNameAlreadyExistException("name");
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.OK, ErrorCode.TAG);

        final ResponseEntity<HttpError> handled = handler.handleTagNameAlreadyExistException(exception, null);

        assertTrue(handled.hasBody());
        assertFalse(httpError.equals(handled.getBody()));
        assertNotEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void successGiftCertificateSearchParameterNotProvidedExceptionHandler() {
        GiftCertificateSearchParameterNotProvidedException exception = new GiftCertificateSearchParameterNotProvidedException();
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.BAD_REQUEST, ErrorCode.GIFT_CERTIFICATE);

        final ResponseEntity<HttpError> handled = handler.handleGiftCertificateSearchProviderNotProvidedException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void failGiftCertificateSearchParameterNotProvidedExceptionHandlerWithDifferentStatus() {
        GiftCertificateSearchParameterNotProvidedException exception = new GiftCertificateSearchParameterNotProvidedException();
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.OK, ErrorCode.GIFT_CERTIFICATE);

        final ResponseEntity<HttpError> handled = handler.handleGiftCertificateSearchProviderNotProvidedException(exception, null);

        assertTrue(handled.hasBody());
        assertFalse(httpError.equals(handled.getBody()));
        assertNotEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void successUserNotFoundExceptionHandlerById() {
        UserNotFoundException exception = new UserNotFoundException(1);
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.USER);

        final ResponseEntity<HttpError> handled = handler.handleUserNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void successUserNotFoundExceptionHandlerByEmail() {
        UserNotFoundException exception = new UserNotFoundException("mail@mail.ru");
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.USER);

        final ResponseEntity<HttpError> handled = handler.handleUserNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void failUserNotFoundExceptionHandlerWithDifferentStatus() {
        UserNotFoundException exception = new UserNotFoundException(1);
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.OK, ErrorCode.USER);

        final ResponseEntity<HttpError> handled = handler.handleUserNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertFalse(httpError.equals(handled.getBody()));
        assertNotEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void successOrderNotFoundExceptionHandler() {
        OrderNotFoundException exception = new OrderNotFoundException();
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.ORDER);

        final ResponseEntity<HttpError> handled = handler.handleOrderNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertTrue(httpError.equals(handled.getBody()));
        assertEquals(httpError.getStatus(), handled.getStatusCode());
    }

    @Test
    void failOrderNotFoundExceptionHandlerWithDifferentStatus() {
        OrderNotFoundException exception = new OrderNotFoundException();
        HttpError httpError = new HttpErrorImpl(exception.getMessage(), HttpStatus.OK, ErrorCode.ORDER);

        final ResponseEntity<HttpError> handled = handler.handleOrderNotFoundException(exception, null);

        assertTrue(handled.hasBody());
        assertFalse(httpError.equals(handled.getBody()));
        assertNotEquals(httpError.getStatus(), handled.getStatusCode());
    }
}
