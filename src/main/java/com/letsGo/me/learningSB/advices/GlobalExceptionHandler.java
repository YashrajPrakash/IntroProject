package com.letsGo.me.learningSB.advices;

import com.letsGo.me.learningSB.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException exception){
       ApiError error = ApiError.builder()
                            .status(HttpStatus.NOT_FOUND)
                                .message(exception.getMessage())
                                    .build();
       return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalServerError(Exception exception){
        ApiError error = ApiError.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .message(exception.getMessage())
                                    .build();
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInputValidationErrors(MethodArgumentNotValidException exception){
        List<String> errors = exception
                                .getBindingResult()
                                    .getAllErrors()
                                        .stream()
                                            .map(error -> error.getDefaultMessage())
                                                .toList();

        ApiError apiError = ApiError.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                    .message("Input Validation failed")
                                        .subErrors(errors)
                                            .build();

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
