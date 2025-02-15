package com.example.news_feed.common.error.controller;

import com.example.news_feed.common.error.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> handleException401(Exception400 ex){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorMessage, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> handleException401(Exception401 ex){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorMessage, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> handleException403(Exception403 ex){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorMessage, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> handleException404(Exception404 ex){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorMessage, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> handleException409(Exception409 ex){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorMessage, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
