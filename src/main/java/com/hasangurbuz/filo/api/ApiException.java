package com.hasangurbuz.filo.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private String code;
    private String message;

    public static ApiException accessDenied() {
        ApiException ex = new ApiException();
        ex.setCode(ApiExceptionCode.ACCESS_DENIED);
        ex.setMessage("You have not access");
        return ex;
    }

    public static ApiException invalidInput(String message) {
        ApiException ex = new ApiException();
        ex.setCode(ApiExceptionCode.INVALID_INPUT);
        ex.setMessage(message);
        return ex;
    }

    public static ApiException notFound(String message) {
        ApiException ex = new ApiException();
        ex.setCode(ApiExceptionCode.NOT_FOUND);
        ex.setMessage(message);
        return ex;
    }

}
