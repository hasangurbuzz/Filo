package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.dto.VehicleDto;

public interface VehicleService {
    Vehicle addVehicle(VehicleDto vehicleRequest);

    void deleteVehicle(Long vehicleId);
}
