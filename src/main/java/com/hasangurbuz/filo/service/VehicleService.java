package com.hasangurbuz.filo.service;

import com.hasangurbuz.filo.domain.Vehicle;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);

    Vehicle update(Vehicle vehicle);

    Vehicle get(Long vehicleId, Long companyId);

    void delete(Vehicle vehicle);

    boolean existsPlateNumber(Long companyId, String plateNumber);

    boolean existsChassisNumber(Long companyId, String chassisNumber);
}
