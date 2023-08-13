package com.hasangurbuz.filo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "T_Vehicle")
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
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

    @Column(name = "creation_date", nullable = false)
    private OffsetDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;
}
