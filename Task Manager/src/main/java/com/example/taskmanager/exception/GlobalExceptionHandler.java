package com.example.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {

        String message = e.getMessage() != null ? e.getMessage() : "Something went wrong";

        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (message.contains("not found")) status = HttpStatus.NOT_FOUND;
        if (message.contains("already taken") || message.contains("Duplicate")) status = HttpStatus.CONFLICT;
        if (message.contains("Invalid password")) status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}