package com.nevrcode.expense_tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.nevrcode.expense_tracker.model.WebResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice 
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintsViolationException(ConstraintViolationException e) {
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(WebResponse.<String>builder()
        .error(e.getMessage()).build());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(WebResponse.<String>builder()
                    .error(e.getReason())
                    .build());
    
    }

//     @ExceptionHandler(Exception.class)
// public ResponseEntity<WebResponse<String>> handleGeneralException(Exception e) {
//     return ResponseEntity
//             .status(HttpStatus.INTERNAL_SERVER_ERROR)
//             .body(WebResponse.<String>builder()
//                     .error(e.getMessage())
//                     .build());
// }

}
