package com.hasangurbuz.vehiclemanager.api.mapper;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import org.openapitools.model.VehicleActionAccessDTO;
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

        if (ApiContext.get().getUserRole() == UserRole.COMPANY_ADMIN) {
            VehicleActionAccessDTO actionAccess = new VehicleActionAccessDTO();
            actionAccess.setUpdate(true);
            actionAccess.setDelete(true);
            actionAccess.setManageUsers(true);
            dto.setActionAccess(actionAccess);
        }
        return dto;
    }

    @Override
    public List<VehicleDTO> toDtoList(List<VehicleAuthority> entityList) {
        List<VehicleDTO> dtoList = new ArrayList<>();
        for (VehicleAuthority authority : entityList) {
            VehicleDTO dto = toDto(authority);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
