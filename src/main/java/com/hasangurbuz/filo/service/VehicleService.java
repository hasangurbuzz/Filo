package com.hasangurbuz.filo.service;

import com.hasangurbuz.filo.domain.Group;
import com.hasangurbuz.filo.domain.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);

    Vehicle update(Vehicle vehicle);

    Vehicle get(Long vehicleId, Long companyId);

    List<Vehicle> findByGroup(Group group);

    void delete(Vehicle vehicle);

    boolean existsPlateNumber(Long companyId, String plateNumber);

    boolean existsChassisNumber(Long companyId, String chassisNumber);
}
