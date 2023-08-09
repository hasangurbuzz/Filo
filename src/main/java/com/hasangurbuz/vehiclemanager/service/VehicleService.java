package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);

    Vehicle updateVehicle(Long id, Vehicle vehicle);

    List<Vehicle> getVehicles();

    Vehicle getVehicleById(Long id);

    void deleteVehicle(Long id);

    boolean existsPlateNumber(Long companyId, String plateNumber);

    boolean existsChassisNumber(Long companyId, String chassisNumber);
}
