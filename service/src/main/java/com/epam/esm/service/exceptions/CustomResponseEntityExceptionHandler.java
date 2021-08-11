package com.epam.esm.service.exceptions;

import com.epam.esm.entity.exceptions.IllegalOrderStatusValueException;
import com.epam.esm.service.error.ErrorCode;
import com.epam.esm.service.error.HttpError;
import com.epam.esm.service.error.HttpErrorImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<HttpError> handleTagNotFoundException(TagNotFoundException e, WebRequest request) {
        HttpError tagNotFoundExceptionResponse = new HttpErrorImpl(e.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.TAG);
        return new ResponseEntity<>(tagNotFoundExceptionResponse, tagNotFoundExceptionResponse.getStatus());
    }

    @ExceptionHandler(TagNameAlreadyExistException.class)
    public ResponseEntity<HttpError> handleTagNameAlreadyExistException(TagNameAlreadyExistException e, WebRequest request) {
        HttpError tagExceptionResponse = new HttpErrorImpl(e.getMessage(), HttpStatus.CONFLICT, ErrorCode.TAG);
        return new ResponseEntity<>(tagExceptionResponse, tagExceptionResponse.getStatus());
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<HttpError> handleGiftCertificateNotFoundException(GiftCertificateNotFoundException e, WebRequest request) {
        HttpError giftCertificateNotFound = new HttpErrorImpl(e.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.GIFT_CERTIFICATE);
        return new ResponseEntity<>(giftCertificateNotFound, giftCertificateNotFound.getStatus());
    }

    @ExceptionHandler(GiftCertificateSearchParameterNotProvidedException.class)
    public ResponseEntity<HttpError> handleGiftCertificateSearchProviderNotProvidedException(GiftCertificateSearchParameterNotProvidedException e, WebRequest request) {
        HttpError giftCertificateSearchProviderNotProvided = new HttpErrorImpl(e.getMessage(), HttpStatus.BAD_REQUEST, ErrorCode.GIFT_CERTIFICATE);
        return new ResponseEntity<>(giftCertificateSearchProviderNotProvided, giftCertificateSearchProviderNotProvided.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpError> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        HttpError userNotFound = new HttpErrorImpl(e.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.USER);
        return new ResponseEntity<>(userNotFound, userNotFound.getStatus());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<HttpError> handleOrderNotFoundException(OrderNotFoundException e, WebRequest request) {
        HttpError orderNotFound = new HttpErrorImpl(e.getMessage(), HttpStatus.NOT_FOUND, ErrorCode.ORDER);
        return new ResponseEntity<>(orderNotFound, orderNotFound.getStatus());
    }

    @ExceptionHandler(IllegalOrderStatusValueException.class)
    public ResponseEntity<HttpError> handleIllegalOrderStatusValueException(IllegalOrderStatusValueException e, WebRequest request) {
        HttpError orderError = new HttpErrorImpl(e.getMessage(), HttpStatus.BAD_REQUEST, ErrorCode.ORDER);
        return new ResponseEntity<>(orderError, orderError.getStatus());
    }
}
