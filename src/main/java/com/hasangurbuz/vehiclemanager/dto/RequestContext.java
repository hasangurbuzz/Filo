package com.hasangurbuz.vehiclemanager.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RequestContext {
    private User user;
}
