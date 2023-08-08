package com.hasangurbuz.vehiclemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    private String companyName;
    private UserRole userRole;
}
