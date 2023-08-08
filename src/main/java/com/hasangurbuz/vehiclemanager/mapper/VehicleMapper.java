package com.hasangurbuz.vehiclemanager.mapper;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.dto.VehicleDto;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper implements Mapper<Vehicle, VehicleDto> {
    @Override
    public Vehicle toEntity(VehicleDto dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setNumberPlate(dto.getNumberPlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setModelYear(dto.getModelYear());
        vehicle.setChassisNumber(dto.getChassisNumber());
        vehicle.setLabel(dto.getLabel());
        return vehicle;
    }

    @Override
    public VehicleDto toDto(Vehicle entity) {
        VehicleDto dto = new VehicleDto();
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setModelYear(entity.getModelYear());
        dto.setLabel(entity.getLabel());
        dto.setNumberPlate(entity.getNumberPlate());
        dto.setChassisNumber(entity.getChassisNumber());
        return dto;
    }
}
