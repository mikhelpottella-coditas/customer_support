package com.example.customersupport.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException ex) {
        logger.error("Custom exception occurred");
        ErrorResponse error = new ErrorResponse(ex.getStatusCode(),
                ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(ex.getStatusCode()).body(error);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map> validationExceptionHandler(MethodArgumentNotValidException ex) {
        logger.warn("Validation error");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e ->
                errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(ex.getStatusCode()).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException ex) {
        logger.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

}
