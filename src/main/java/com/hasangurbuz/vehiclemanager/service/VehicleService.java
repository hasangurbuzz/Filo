package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);

    Vehicle update(Long id, Vehicle vehicle);

    Vehicle getVehicleById(Long id);

    void deleteVehicle(Long id);

    boolean existsPlateNumber(Long companyId, String plateNumber);

    boolean existsChassisNumber(Long companyId, String chassisNumber);
}
