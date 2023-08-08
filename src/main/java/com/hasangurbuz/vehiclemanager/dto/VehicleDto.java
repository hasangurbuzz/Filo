package com.hasangurbuz.vehiclemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDto implements Serializable {
    private String numberPlate;
    private String chassisNumber;
    private String label;
    private String brand;
    private String model;
    private Integer modelYear;
}
