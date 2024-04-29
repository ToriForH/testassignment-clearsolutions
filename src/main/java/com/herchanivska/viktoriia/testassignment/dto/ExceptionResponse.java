package com.herchanivska.viktoriia.testassignment.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class ExceptionResponse {
    HttpStatus status;
    String message;
    String requestURL;
}
