package com.hasangurbuz.vehiclemanager.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private String code;
    private String message;
}
