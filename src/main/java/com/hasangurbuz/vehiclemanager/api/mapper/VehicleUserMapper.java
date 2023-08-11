package com.hasangurbuz.vehiclemanager.api.mapper;

import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import org.openapitools.model.VehicleUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleUserMapper implements Mapper<VehicleAuthority, VehicleUserDTO> {

    @Autowired
    private VehicleMapper vehicleMapper;


    @Override
    public VehicleAuthority toEntity(VehicleUserDTO dto) {
        VehicleAuthority vAuthority = new VehicleAuthority();
        vAuthority.setUserId(dto.getId());
        vAuthority.setRole(vehicleMapper.toUserRole(dto.getRole()));
        return vAuthority;
    }

    @Override
    public VehicleUserDTO toDto(VehicleAuthority entity) {
        VehicleUserDTO vehicleUserDTO = new VehicleUserDTO();
        vehicleUserDTO.setId(entity.getUserId());
        vehicleUserDTO.setRole(vehicleMapper.toUserRoleDto(entity.getRole()));
        return vehicleUserDTO;
    }

    @Override
    public List<VehicleUserDTO> toDtoList(List<VehicleAuthority> entityList) {
        List<VehicleUserDTO> vehicleUserDtoList = new ArrayList<>(entityList.size());
        if (entityList.isEmpty()) {
            return vehicleUserDtoList;
        }
        for (VehicleAuthority vAuthority : entityList) {
            vehicleUserDtoList.add(toDto(vAuthority));
        }
        return vehicleUserDtoList;
    }
}
