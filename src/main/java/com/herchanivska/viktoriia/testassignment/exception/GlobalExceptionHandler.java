package com.herchanivska.viktoriia.testassignment.exception;

import com.herchanivska.viktoriia.testassignment.dto.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ExceptionResponse body = getResponseBody(ex, request, 404);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatusCode.valueOf(404), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ExceptionResponse body = getResponseBody(ex, request, 400);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatusCode.valueOf(400), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception ex, WebRequest request) {
        ExceptionResponse body = getResponseBody(ex, request, 500);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatusCode.valueOf(500), request);
    }

    private ExceptionResponse getResponseBody(Exception ex, WebRequest request, int status) {
        return new ExceptionResponse(HttpStatus.resolve(status), ex.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());
    }
}
