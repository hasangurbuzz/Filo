package com.hasangurbuz.filo.api;

import org.openapitools.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> handle(ApiException ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO();
        error.setCode(ex.getCode());
        error.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleFields(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        if (result.hasErrors()) {
            ErrorDTO error = new ErrorDTO();
            error.setCode(ApiExceptionCode.INVALID_INPUT);
            error.setMessage(result.getFieldError().getField() + " : " + result.getFieldError().getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        return null;
    }
}
