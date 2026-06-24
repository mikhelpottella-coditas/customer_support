package com.example.customersupport.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatusCode statusCode;
    private String message;
    private LocalDateTime localDateTime;
    private String methodName;

}
