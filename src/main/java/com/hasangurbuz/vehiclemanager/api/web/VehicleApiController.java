package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.codehaus.plexus.util.StringUtils;
import org.openapitools.api.VehicleApi;
import org.openapitools.model.UserRoleDTO;
import org.openapitools.model.VehicleCreateRequestDTO;
import org.openapitools.model.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class VehicleApiController implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public ResponseEntity<VehicleDTO> create(VehicleCreateRequestDTO vehicleCreateRequestDTO) {

        if (ApiContext.get().getUserRole() != UserRoleDTO.COMPANYADMIN) {
            throw ApiException.accessDenied();
        }

        if (StringUtils.isBlank(vehicleCreateRequestDTO.getBrand())) {
            throw ApiException.invalidInput("Brand is required");
        }
        if (StringUtils.isBlank(vehicleCreateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Number plate is required");
        }
        if (StringUtils.isBlank(vehicleCreateRequestDTO.getModel())) {
            throw ApiException.invalidInput("Model is required");
        }
        if (vehicleCreateRequestDTO.getModelYear() == null) {
            throw ApiException.invalidInput("Model year is required");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehicleCreateRequestDTO.getBrand());
        vehicle.setTag(vehicleCreateRequestDTO.getTag());
        vehicle.setModel(vehicleCreateRequestDTO.getModel());
        vehicle.setModelYear(vehicleCreateRequestDTO.getModelYear());
        vehicle.setChassisNumber(vehicleCreateRequestDTO.getChassisNumber());
        vehicle.setNumberPlate(vehicleCreateRequestDTO.getNumberPlate());
        vehicle.setCompanyId(ApiContext.get().getCompanyId());


        vehicle = vehicleService.create(vehicle);
        VehicleDTO dto = vehicleMapper.toDto(vehicle);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<VehicleDTO> vehicleIdPost(String id) {
        VehicleDTO vehicleDto = vehicleMapper.toDto(vehicleService.getVehicleById(Long.valueOf(id)));
        return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public ResponseEntity<List<VehicleDTO>> get() {
        return VehicleApi.super.get();
    }

    @Override
    public ResponseEntity<VehicleDTO> update(String id, VehicleDTO vehicleDTO) {
        Vehicle oldVehicle = vehicleMapper.toEntity(vehicleDTO);

        Vehicle updatedVehicle = vehicleService.updateVehicle(Long.valueOf(id), oldVehicle);
        vehicleDTO = vehicleMapper.toDto(updatedVehicle);
        return ResponseEntity.ok(vehicleDTO);
    }



}
