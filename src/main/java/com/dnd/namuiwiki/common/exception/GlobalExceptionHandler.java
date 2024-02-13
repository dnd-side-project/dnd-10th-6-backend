package com.dnd.namuiwiki.common.exception;

import com.dnd.namuiwiki.common.exception.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Primary
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ApplicationErrorException.class)
    public ResponseEntity<?> handleApplicationErrorException(ApplicationErrorException e) {
        log.error("ApplicationErrorException", e);

        String errorMessage = (e.getCustomMessage() != null && !e.getCustomMessage().isEmpty())
                ? e.getCustomMessage()
                : e.getMessage();
        var response = new ErrorResponseDto(e.getErrorType().name(), errorMessage);
        return new ResponseEntity<>(response, e.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        log.error("ValidationException", e);
        var response = new ErrorResponseDto(ApplicationErrorType.INVALID_DATA_ARGUMENT.name(), e.getMessage());
        return new ResponseEntity<>(response, ApplicationErrorType.INVALID_DATA_ARGUMENT.getHttpStatus());
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<?> handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.error("HttpMessageConversionException", e);
        var response = new ErrorResponseDto(ApplicationErrorType.INVALID_DATA_ARGUMENT.name(), e.getMessage());
        return new ResponseEntity<>(response, ApplicationErrorType.INVALID_DATA_ARGUMENT.getHttpStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException", e);
        var response = new ErrorResponseDto(ApplicationErrorType.INVALID_DATA_ARGUMENT.name(), e.getMessage());
        return new ResponseEntity<>(response, ApplicationErrorType.INVALID_DATA_ARGUMENT.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Exception", e);
        var response = new ErrorResponseDto(ApplicationErrorType.INTERNAL_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(response, ApplicationErrorType.INTERNAL_ERROR.getHttpStatus());
    }
}
