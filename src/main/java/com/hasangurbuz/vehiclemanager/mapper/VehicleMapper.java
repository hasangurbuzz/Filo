package com.hasangurbuz.vehiclemanager.mapper;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import org.openapitools.model.VehicleDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleMapper implements Mapper<Vehicle, VehicleDto> {
    @Override
    public Vehicle toEntity(VehicleDto dto) {
        if (dto == null) return null;

        Vehicle vehicle = new Vehicle();
        vehicle.setNumberPlate(dto.getNumberPlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setModelYear(dto.getModelYear());
        vehicle.setChassisNumber(dto.getChassisNumber());
        vehicle.setTag(dto.getTag());
        return vehicle;
    }

    @Override
    public VehicleDto toDto(Vehicle entity) {
        if (entity == null) return null;
        VehicleDto dto = new VehicleDto();
        dto.setId(entity.getId().toString());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setModelYear(entity.getModelYear());
        dto.setTag(entity.getTag());
        dto.setNumberPlate(entity.getNumberPlate());
        dto.setChassisNumber(entity.getChassisNumber());
        return dto;
    }

    @Override
    public List<VehicleDto> toDtoList(List<Vehicle> entityList) {
        List<VehicleDto> vehicleDtoList = new ArrayList<>(entityList.size());
        for (Vehicle vehicle : entityList) {
            vehicleDtoList.add(toDto(vehicle));
        }
        return vehicleDtoList;
    }


}
