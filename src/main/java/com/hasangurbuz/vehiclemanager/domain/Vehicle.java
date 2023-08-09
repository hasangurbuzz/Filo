package com.hasangurbuz.vehiclemanager.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_Vehicle")
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_plate", nullable = false)
    private String numberPlate;

    @Column(name = "chassis_number")
    private String chassisNumber;

    @Column(name = "tag")
    private String tag;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
