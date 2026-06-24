package com.example.customersupport.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private final HttpStatusCode statusCode;
    private final String message;

    public CustomException( HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}
