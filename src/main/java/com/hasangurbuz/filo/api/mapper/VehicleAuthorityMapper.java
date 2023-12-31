package com.hasangurbuz.filo.api.mapper;

import com.hasangurbuz.filo.domain.Vehicle;
import com.hasangurbuz.filo.domain.VehicleAuthority;
import org.openapitools.model.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleAuthorityMapper implements Mapper<VehicleAuthority, VehicleDTO> {

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public VehicleAuthority toEntity(VehicleDTO dto) {
        VehicleAuthority authority = new VehicleAuthority();
        Vehicle vehicle = vehicleMapper.toEntity(dto);
        authority.setVehicle(vehicle);
        return authority;
    }

    @Override
    public VehicleDTO toDto(VehicleAuthority entity) {
        Vehicle vehicle = entity.getVehicle();
        VehicleDTO dto = vehicleMapper.toDto(vehicle);
        return dto;
    }

    @Override
    public List<VehicleDTO> toDtoList(List<VehicleAuthority> entityList) {
        List<VehicleDTO> dtoList = new ArrayList<>();
        if (entityList.isEmpty()) {
            return dtoList;
        }

        for (VehicleAuthority authority : entityList) {
            VehicleDTO dto = toDto(authority);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Vehicle toVehicle(VehicleAuthority entity) {
        return entity.getVehicle();
    }

    public List<Vehicle> toVehicleList(List<VehicleAuthority> entityList) {
        List<Vehicle> vehicles = new ArrayList<>(entityList.size());
        for (VehicleAuthority v : entityList) {
            vehicles.add(toVehicle(v));
        }
        return vehicles;
    }
}
