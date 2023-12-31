package com.hasangurbuz.filo.api.mapper;

import com.hasangurbuz.filo.domain.UserRole;
import com.hasangurbuz.filo.domain.Vehicle;
import org.openapitools.model.UserRoleDTO;
import org.openapitools.model.VehicleDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleMapper implements Mapper<Vehicle, VehicleDTO> {
    @Override
    public Vehicle toEntity(VehicleDTO dto) {
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
    public VehicleDTO toDto(Vehicle entity) {
        if (entity == null) return null;
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getId().toString());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setModelYear(entity.getModelYear());
        dto.setTag(entity.getTag());
        dto.setNumberPlate(entity.getNumberPlate());
        dto.setChassisNumber(entity.getChassisNumber());
        dto.setCreationDate(entity.getCreationDate());
        dto.setGroupId(entity.getGroup().getId());
        dto.setGroupName(entity.getGroup().getName());
        return dto;
    }

    @Override
    public List<VehicleDTO> toDtoList(List<Vehicle> entityList) {
        List<VehicleDTO> vehicleDtoList = new ArrayList<>(entityList.size());
        if (entityList.isEmpty()) {
            return vehicleDtoList;
        }
        for (Vehicle vehicle : entityList) {
            vehicleDtoList.add(toDto(vehicle));
        }
        return vehicleDtoList;
    }

    public UserRoleDTO toUserRoleDto(UserRole userRole) {
        return UserRoleDTO.valueOf(userRole.getValue());
    }

    public UserRole toUserRole(UserRoleDTO dto) {
        return UserRole.valueOf(dto.getValue());
    }


}
