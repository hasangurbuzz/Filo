package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.repository.VehicleRepository;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Vehicle create(Vehicle vehicle) {
        vehicle.setIsDeleted(false);
        vehicle = vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle storedVehicle = getVehicleById(id);
        if (storedVehicle != null) {
            storedVehicle.setBrand(vehicle.getBrand());
            storedVehicle.setChassisNumber(vehicle.getChassisNumber());
            storedVehicle.setModel(vehicle.getModel());
            storedVehicle.setModelYear(vehicle.getModelYear());
            storedVehicle.setNumberPlate(vehicle.getNumberPlate());
            storedVehicle.setTag(vehicle.getTag());

            return vehicleRepository.save(storedVehicle);
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = vehicleRepository
                .findVehicleByCompanyIdAndIsDeletedFalse(ApiContext.get().getCompanyId());
        return vehicles;
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository
                .findVehicleByIdAndCompanyIdAndIsDeletedFalse(id, ApiContext.get().getCompanyId());
        return vehicle;
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        if (vehicle != null) {
            vehicle.setIsDeleted(true);
            vehicleRepository.save(vehicle);
        }
    }

}
